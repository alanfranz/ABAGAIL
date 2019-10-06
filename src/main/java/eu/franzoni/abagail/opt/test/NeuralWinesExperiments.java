package eu.franzoni.abagail.opt.test;

import com.sun.org.apache.regexp.internal.RE;
import eu.franzoni.abagail.func.nn.activation.Rectifier;
import eu.franzoni.abagail.func.nn.backprop.BackPropagationNetwork;
import eu.franzoni.abagail.func.nn.backprop.BackPropagationNetworkFactory;
import eu.franzoni.abagail.func.nn.backprop.BatchBackPropagationTrainer;
import eu.franzoni.abagail.func.nn.backprop.RPROPUpdateRule;
import eu.franzoni.abagail.func.nn.feedfwd.FeedForwardNetwork;
import eu.franzoni.abagail.func.nn.feedfwd.FeedForwardNeuralNetworkFactory;
import eu.franzoni.abagail.opt.OptimizationAlgorithm;
import eu.franzoni.abagail.opt.RandomizedHillClimbing;
import eu.franzoni.abagail.opt.SimulatedAnnealing;
import eu.franzoni.abagail.opt.example.NeuralNetworkOptimizationProblem;
import eu.franzoni.abagail.opt.ga.StandardGeneticAlgorithm;
import eu.franzoni.abagail.shared.*;
import eu.franzoni.abagail.shared.tester.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.function.Function;

/**
 * Implementation of randomized hill climbing, simulated annealing, and genetic algorithm to
 * find optimal weights to a neural network that is classifying abalone as having either fewer 
 * or more than 15 rings. 
 *
 * @author Hannah Lau
 * @version 1.0
 */
public class NeuralWinesExperiments {
    private static final int FEATURE_COUNT = 11;

    private static final int[] LAYERS = new int[]{ FEATURE_COUNT, 10, 5, 1};

    private static Instance[] trainingInstances = parseInstancesFromFile(3918, "whitewines-zscored-train.csv");
    private static Instance[] testInstances = parseInstancesFromFile(980, "whitewines-zscored-test.csv");

    private static BackPropagationNetworkFactory factory = new BackPropagationNetworkFactory();
    
    private static ErrorMeasure measure = new SumOfSquaresError();

    private static DataSet set = new DataSet(trainingInstances);

    private static String[] oaNames = {"RHC", "SA", "GA", "BACKPROP"};
    private static BackPropagationNetwork networks[] = new BackPropagationNetwork[3];
    private static NeuralNetworkOptimizationProblem[] nnop = new NeuralNetworkOptimizationProblem[3];

    private static OptimizationAlgorithm[] oa = new OptimizationAlgorithm[3];

    private static String results = "";

    private static DecimalFormat df = new DecimalFormat("0.000");

    public static void main(String[] args) {
        MyRandom.initialize(12354);
        System.out.println("BACKPROP " + System.nanoTime());
        trainWithBackpropagation(1000);
        System.out.println("RHC " + System.nanoTime());
        trainFeedForwardWithOptimizationAlgorithm(nno -> new RandomizedHillClimbing(nno), 10000);
        System.out.println("GA " + System.nanoTime());
        trainFeedForwardWithOptimizationAlgorithm(nno ->new StandardGeneticAlgorithm(4000, 200, 200, nno), 10000);
        System.out.println("SA " + System.nanoTime());
        trainFeedForwardWithOptimizationAlgorithm(nno ->new SimulatedAnnealing(1e11, 0.95, nno), 10000);
    }


    private static Instance[] parseInstancesFromFile(final int lineCount, String filename) {
        double[][][] attributes = new double[lineCount][][];

        try {
            BufferedReader br = new BufferedReader(new FileReader(NeuralNetworkThings.class.getResource(
                    filename).getFile()));

            for (int i = 0; i < attributes.length; i++) {
                Scanner scan = new Scanner(br.readLine());
                scan.useDelimiter(",");


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
            instances[i].setLabel(new Instance(attributes[i][1][0]));
        }

        return instances;
    }


    public static void trainFeedForwardWithOptimizationAlgorithm(Function<NeuralNetworkOptimizationProblem, OptimizationAlgorithm> optimizationFactory, int iterations) {

//        }

        int[] labels = {0, 1};
        final Instance[] patterns = trainingInstances;

        // 2) Instantiate a network using the FeedForwardNeuralNetworkFactory.  This network
        //    will be our classifier.
        FeedForwardNeuralNetworkFactory factory = new FeedForwardNeuralNetworkFactory();
        // 2a) These numbers correspond to the number of nodes in each layer.
        //     This network has 4 input nodes, 3 hidden nodes in 1 layer, and 1 output node in the output layer.
        FeedForwardNetwork network = factory.createClassificationNetwork(new int[] { FEATURE_COUNT, 10, 5, 1 }, new Rectifier());

        // 3) Instantiate a measure, which is used to evaluate each possible set of weights.
        ErrorMeasure measure = new SumOfSquaresError();

        // 4) Instantiate a DataSet, which adapts a set of instances to the optimization problem.
        DataSet set = new DataSet(patterns);

        // 5) Instantiate an optimization problem, which is used to specify the dataset, evaluation
        //    function, mutator and crossover function (for Genetic Algorithms), and any other
        //    parameters used in optimization.
        NeuralNetworkOptimizationProblem nno = new NeuralNetworkOptimizationProblem(
                set, network, measure);

        // 6) Instantiate a specific OptimizationAlgorithm, which defines how we pick our next potential
        //    hypothesis.
        OptimizationAlgorithm o =optimizationFactory.apply(nno);
        System.out.printf("OptimizationAlgorithm: %s", o);


        // 7) Instantiate a trainer.  The FixtIterationTrainer takes another trainer (in this case,
        //    an OptimizationAlgorithm) and executes it a specified number of times.
        FixedIterationTrainer fit = new FixedIterationTrainer(o, iterations);

        // 8) Run the trainer.  This may take a little while to run, depending on the OptimizationAlgorithm,
        //    size of the data, and number of iterations.
        long howlong = benchmarkMillis(fit);

        // 9) Once training is done, get the optimal solution from the OptimizationAlgorithm.  These are the
        //    optimal weights found for this network.
        Instance opt = o.getOptimal();
        network.setWeights(opt.getData());

        //10) Run the training data through the network with the weights discovered through optimization, and
        //    print out the expected label and result of the classifier for each instance.
        TestMetric acc = new AccuracyTestMetric();
        TestMetric cm  = new ConfusionMatrixTestMetric(labels);
        Tester t = new BinaryOutputNeuralNetworkTester(network, acc, cm);
        t.test(patterns);

        acc.printResults();
        cm.printResults();
    }

    public static void trainWithBackpropagation(int iterations) {
        int[] labels = new int[] {0,1};
        BackPropagationNetworkFactory factory =
                new BackPropagationNetworkFactory();

        Instance[] patterns = trainingInstances;
        BackPropagationNetwork network = factory.createClassificationNetwork(
                LAYERS, new Rectifier());
        SumOfSquaresError measure = new SumOfSquaresError();
        DataSet set = new DataSet(patterns);
        NeuralNetworkOptimizationProblem nno = new NeuralNetworkOptimizationProblem(
                set, network, measure);
        BatchBackPropagationTrainer batchBackPropagationTrainer = new BatchBackPropagationTrainer(set, network, measure, new RPROPUpdateRule(0.01, 50, 0.00001));
        FixedIterationTrainer fit = new FixedIterationTrainer(batchBackPropagationTrainer, 5000);
        fit.train();
        double[] weights = network.getWeights();
        //network.setWeights(opt.getData());

        //10) Run the training data through the network with the weights discovered through optimization, and
        //    print out the expected label and result of the classifier for each instance.
        TestMetric acc = new AccuracyTestMetric();
        TestMetric cm  = new ConfusionMatrixTestMetric(labels);
        Tester t = new BinaryOutputNeuralNetworkTester(network, acc, cm);
        t.test(patterns);

        acc.printResults();
        cm.printResults();
    }

    private static long benchmarkMillis(Trainer trainer) {
        long before = System.currentTimeMillis();
        trainer.train();
        return System.currentTimeMillis() - before;
    }

//    public static void old() {
//        MyRandom.initialize(12345);
//
//        final int inputLayer = 11;
//        final int outputLayer = 1;
//        final int trainingIterations = 10;
//
//        for(int i = 0; i < oa.length; i++) {
//            networks[i] = factory.createClassificationNetwork(
//                new int[] {inputLayer, 10, 10, 10, outputLayer}, new Rectifier());
//            nnop[i] = new NeuralNetworkOptimizationProblem(set, networks[i], measure);
//        }
//
//        oa[0] = new RandomizedHillClimbing(nnop[0]);
//        oa[1] = new SimulatedAnnealing(1E11, .95, nnop[1]);
//        oa[2] = new StandardGeneticAlgorithm(200, 100, 10, nnop[2]);
//
//        for(int i = 0; i < oa.length; i++) {
//            double start = System.nanoTime(), end, trainingTime, testingTime, correct = 0, incorrect = 0;
//            train(oa[i], networks[i], oaNames[i], trainingIterations); //trainer.train();
//            end = System.nanoTime();
//            trainingTime = end - start;
//            trainingTime /= Math.pow(10,9);
//
//            Instance optimalInstance = oa[i].getOptimal();
//            networks[i].setWeights(optimalInstance.getData());
//
//            double predicted, actual;
//            start = System.nanoTime();
//            for(int j = 0; j < trainingInstances.length; j++) {
//                networks[i].setInputValues(trainingInstances[j].getData());
//                networks[i].run();
//
//                predicted = Double.parseDouble(trainingInstances[j].getLabel().toString());
//                actual = Double.parseDouble(networks[i].getOutputValues().toString());
//
//                double trash = Math.abs(predicted - actual) < 0.5 ? correct++ : incorrect++;
//
//            }
//            end = System.nanoTime();
//            testingTime = end - start;
//            testingTime /= Math.pow(10,9);
//
//            results +=  "\nResults for " + oaNames[i] + ": \nCorrectly classified " + correct + " trainingInstances." +
//                        "\nIncorrectly classified " + incorrect + " trainingInstances.\nPercent correctly classified: "
//                        + df.format(correct/(correct+incorrect)*100) + "%\nTraining time: " + df.format(trainingTime)
//                        + " seconds\nTesting time: " + df.format(testingTime) + " seconds\n";
//        }
//
//        System.out.println(results);
//    }
//
//    private static void train(OptimizationAlgorithm oa, BackPropagationNetwork network, String oaName, final int trainingIterations) {
//        System.out.println("\nError results for " + oaName + "\n---------------------------");
//
//        for(int i = 0; i < trainingIterations; i++) {
//            oa.train();
//
//            double error = 0;
//            for(int j = 0; j < trainingInstances.length; j++) {
//                network.setInputValues(trainingInstances[j].getData());
//                network.run();
//
//                Instance output = trainingInstances[j].getLabel();
//                Instance example = new Instance(network.getOutputValues());
//                example.setLabel(new Instance(Double.parseDouble(network.getOutputValues().toString())));
//                error += measure.value(output, example);
//            }
//
//            System.out.println(df.format(error));
//        }
//    }
//
//    private static Instance[] initializeInstances() {
//
//        double[][][] attributes = new double[4177][][];
//
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(NeuralWinesExperiments.class.getResource("abalone.txt").getFile()));
//
//            for(int i = 0; i < attributes.length; i++) {
//                Scanner scan = new Scanner(br.readLine());
//                scan.useDelimiter(",");
//
//                attributes[i] = new double[2][];
//                attributes[i][0] = new double[7]; // 7 attributes
//                attributes[i][1] = new double[1];
//
//                for(int j = 0; j < 7; j++)
//                    attributes[i][0][j] = Double.parseDouble(scan.next());
//
//                attributes[i][1][0] = Double.parseDouble(scan.next());
//            }
//        }
//        catch(Exception e) {
//            e.printStackTrace();
//        }
//
//        Instance[] instances = new Instance[attributes.length];
//
//        for(int i = 0; i < instances.length; i++) {
//            instances[i] = new Instance(attributes[i][0]);
//            // classifications range from 0 to 30; split into 0 - 14 and 15 - 30
//            instances[i].setLabel(new Instance(attributes[i][1][0] < 15 ? 0 : 1));
//        }
//
//        return instances;
//    }



}
