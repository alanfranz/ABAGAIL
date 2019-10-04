package eu.franzoni.abagail.opt.test;

import eu.franzoni.abagail.func.nn.activation.Rectifier;
import eu.franzoni.abagail.func.nn.backprop.BackPropagationNetwork;
import eu.franzoni.abagail.func.nn.backprop.BackPropagationNetworkFactory;
import eu.franzoni.abagail.func.nn.backprop.BatchBackPropagationTrainer;
import eu.franzoni.abagail.func.nn.backprop.RPROPUpdateRule;
import eu.franzoni.abagail.opt.OptimizationAlgorithm;
import eu.franzoni.abagail.opt.RandomizedHillClimbing;
import eu.franzoni.abagail.opt.SimulatedAnnealing;
import eu.franzoni.abagail.opt.example.NeuralNetworkOptimizationProblem;
import eu.franzoni.abagail.opt.ga.StandardGeneticAlgorithm;
import eu.franzoni.abagail.shared.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.Scanner;

import static com.sun.tools.doclint.Entity.and;

// Derived from AbaloneTest
public class NeuralNetworkThings {
    private static Instance[] instances = initializeTrainingInstances();

    private static int inputLayer = 11, hiddenLayer = 5, outputLayer = 1, trainingIterations = 1000;
    private static BackPropagationNetworkFactory factory = new BackPropagationNetworkFactory();

    private static ErrorMeasure measure = new SumOfSquaresError();

    private static DataSet set = new DataSet(instances);

    private static BackPropagationNetwork networks[] = new BackPropagationNetwork[3];
    private static NeuralNetworkOptimizationProblem[] nnop = new NeuralNetworkOptimizationProblem[3];

    private static OptimizationAlgorithm[] oa = new OptimizationAlgorithm[3];
    private static String[] oaNames = {"RHC", "SA", "GA"};
    private static String results = "";

    private static DecimalFormat df = new DecimalFormat("0.000");

    public static void main(String[] args) {
        // backprop
        MyRandom.initialize(1234);
//        BackPropagationNetwork classificationNetwork = factory.createClassificationNetwork(
//                new int[]{inputLayer, hiddenLayer, outputLayer});
        final SumOfSquaresError sumOfSquaresError = new SumOfSquaresError();

        Instance[] instances = initializeTrainingInstances();
        final Rectifier rectifier = new Rectifier();
        DataSet dataset = new DataSet(instances);

        BackPropagationNetwork classificationNetwork1 = factory.createClassificationNetwork(
                new int[]{inputLayer, 100, outputLayer}
        );

        BatchBackPropagationTrainer batchBackPropagationTrainer = new BatchBackPropagationTrainer(dataset,
                classificationNetwork1,
                sumOfSquaresError,
                new RPROPUpdateRule()

        );

        FixedIterationTrainer fixedIterationTrainer = new FixedIterationTrainer(batchBackPropagationTrainer, 1000);
        fixedIterationTrainer.train();


        System.out.printf("asd");

//        training_ints = initialize_instances(TRAIN_DATA_FILE)
//        testing_ints = initialize_instances(TEST_DATA_FILE)
//        validation_ints = initialize_instances(VALIDATE_DATA_FILE)
//        factory = BackPropagationNetworkFactory()
//        measure = SumOfSquaresError()
//        data_set = DataSet(training_ints)
//        relu = RELU()
//    # 50 and 0.000001 are the defaults from RPROPUpdateRule.java
//                rule = RPROPUpdateRule(0.064, 50, 0.000001)
//        oa_names = ["Backprop"]
//        classification_network = factory.createClassificationNetwork(
//                [INPUT_LAYER, HIDDEN_LAYER1, HIDDEN_LAYER2, OUTPUT_LAYER], relu)
//        train(BatchBackPropagationTrainer(data_set, classification_network, measure, rule), classification_network,
//                'Backprop', training_ints, validation_ints, testing_ints, measure, TRAINING_ITERATIONS,
//                OUTFILE.format('Backprop'))



    }

    private void trainWithOptimizationAlgorithms() {
        for (int i = 0; i < oa.length; i++) {
            networks[i] = factory.createClassificationNetwork(
                    new int[]{inputLayer, hiddenLayer, outputLayer});
            nnop[i] = new NeuralNetworkOptimizationProblem(set, networks[i], measure);
        }

        oa[0] = new RandomizedHillClimbing(nnop[0]);
        oa[1] = new SimulatedAnnealing(1E11, .95, nnop[1]);
        oa[2] = new StandardGeneticAlgorithm(200, 100, 10, nnop[2]);

        for (int i = 0; i < 1; i++) {
            double start = System.nanoTime(), end, trainingTime, testingTime, correct = 0, incorrect = 0;
            train(oa[i], networks[i], oaNames[i]); //trainer.train();
            end = System.nanoTime();
            trainingTime = end - start;
            trainingTime /= Math.pow(10, 9);

            Instance optimalInstance = oa[i].getOptimal();
            networks[i].setWeights(optimalInstance.getData());

            double predicted, actual;
            start = System.nanoTime();
            for (int j = 0; j < instances.length; j++) {
                networks[i].setInputValues(instances[j].getData());
                networks[i].run();

                predicted = Double.parseDouble(instances[j].getLabel().toString());
                actual = Double.parseDouble(networks[i].getOutputValues().toString());

                double trash = Math.abs(predicted - actual) < 0.5 ? correct++ : incorrect++;

            }
            end = System.nanoTime();
            testingTime = end - start;
            testingTime /= Math.pow(10, 9);

            results += "\nResults for " + oaNames[i] + ": \nCorrectly classified " + correct + " instances." +
                    "\nIncorrectly classified " + incorrect + " instances.\nPercent correctly classified: "
                    + df.format(correct / (correct + incorrect) * 100) + "%\nTraining time: " + df.format(trainingTime)
                    + " seconds\nTesting time: " + df.format(testingTime) + " seconds\n";
        }

        System.out.println(results);


    }

    private static void train(OptimizationAlgorithm oa, BackPropagationNetwork network, String oaName) {
        System.out.println("\nError results for " + oaName + "\n---------------------------");

        for (int i = 0; i < trainingIterations; i++) {
            oa.train();

            double error = 0;
            for (int j = 0; j < instances.length; j++) {
                network.setInputValues(instances[j].getData());
                network.run();

                Instance output = instances[j].getLabel(), example = new Instance(network.getOutputValues());
                example.setLabel(new Instance(Double.parseDouble(network.getOutputValues().toString())));
                error += measure.value(output, example);
            }

            System.out.println(df.format(error));
        }
    }

    private static Instance[] initializeTrainingInstances() {

        double[][][] attributes = new double[3918][][];

        try {
            BufferedReader br = new BufferedReader(new FileReader(NeuralNetworkThings.class.getResource(
                    "whitewines-zscored-train.csv").getFile()));

            for (int i = 0; i < attributes.length; i++) {
                Scanner scan = new Scanner(br.readLine());
                scan.useDelimiter(",");

                final int FEATURE_COUNT = 11;

                attributes[i] = new double[2][];
                attributes[i][0] = new double[FEATURE_COUNT]; // 7 attributes
                attributes[i][1] = new double[1];

                for (int j = 0; j < FEATURE_COUNT; j++)
                    attributes[i][0][j] = Double.parseDouble(scan.next());

                attributes[i][1][0] = Double.parseDouble(scan.next());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Instance[] instances = new Instance[attributes.length];

        for (int i = 0; i < instances.length; i++) {
            instances[i] = new Instance(attributes[i][0]);
            // classifications range from 0 to 30; split into 0 - 14 and 15 - 30
            instances[i].setLabel(new Instance(attributes[i][1][0]));
        }

        return instances;
    }
}
