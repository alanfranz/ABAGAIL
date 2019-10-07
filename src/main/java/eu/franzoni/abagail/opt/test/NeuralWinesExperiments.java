package eu.franzoni.abagail.opt.test;

import com.sun.org.apache.regexp.internal.RE;
import eu.franzoni.abagail.func.nn.activation.Rectifier;
import eu.franzoni.abagail.func.nn.backprop.BackPropagationNetwork;
import eu.franzoni.abagail.func.nn.backprop.BackPropagationNetworkFactory;
import eu.franzoni.abagail.func.nn.backprop.BatchBackPropagationTrainer;
import eu.franzoni.abagail.func.nn.backprop.RPROPUpdateRule;
import eu.franzoni.abagail.func.nn.feedfwd.FeedForwardNetwork;
import eu.franzoni.abagail.func.nn.feedfwd.FeedForwardNeuralNetworkFactory;
import eu.franzoni.abagail.opt.EvaluationFunction;
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
import java.util.Arrays;
import java.util.List;
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

    private static final int[] LAYERS = new int[]{FEATURE_COUNT, 10, 5, 1};

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
        MyRandom.initialize(3537010315L);
        final List<Integer> maxIterationOptions = Arrays.asList(500, 1000, 5000, 10000);
//        final List<Long> seeds = Arrays.asList(13529634494442651L, 5656981922355810402L
//                , -6449267216724225329L, -4411830362201525141L, 5566798682235319198L, -8758420082094094714L, 3253005473232577707L, -5286286812144096374L, -4119452930045979494L, 5770958020097602318L, 683138933608733263L, 8209964896825328468L, 7031392117777361361L, -6121210489028553847L, -6469731346477625132L, -6665466218042342981L, -5961282655162978163L, 1696770048611725543L, 8029254822252000300L, -942868935122628217L, -2724805368243433498L, 6744958667841432491L, 8992788776112062706L, -267750365740541309L, -4609461931086629707L, -6346129594062662673L, 8303094097110875302L, 8534572557185839709L, -1513711453533974150L, -1988507183983714377L,
//                -4771493155960262495L, -1606160883395982541L, 2578644751320052306L, 9110929450740901209L, 717207449707688923L, 2848358714595589750L, -3302511082686917161L, 559566510816032781L, -6245497221226436475L,
//                -6040228022713241920L, -5942417874383785695L, -8729688670016265721L, -4461026529614911420L, 4216892117723149175L, 7183285063220747610L, 5718428447980185135L, 3231711550978164945L, -7674679440764368227L,
//                2085066947842327304L, -4899074866717547002L, -4778814035843079912L, 7783047912961912602L, -6926031667219542166L, 636470299668616838L, -6320072164692178048L, 1641938784710373347L, 443931928227223757L,
//                -5177123061854887676L, 6439515748840354508L, -4635503008972482907L, 3330546935734397669L, -4971350913261785778L, 3937455567930511370L, -4709599159417791981L, -6315455503296899339L, 6917820859638531499L,
//                -2472817110656597739L, 6084103684703190395L, 1403470006669503441L, -6027930348546990837L, 2857457684571945049L, 8422507991458925309L, 97320894082290686L, 787151114575790035L, 1183645201713906288L,
//                -4772122828044506937L, 6370189761144798003L, -7665891922111399228L, -7330054334128328316L, -5550499909978860302L, 1534280183352406632L, -8636587672647994597L, -2468555069095201964L,
//                -9207226534984257761L, -7673201591837605072L, 865598175074152134L, -7256334452322277028L, -7475208512913142686L, -6924067848650035036L, -6415543556141845702L, -640014635566371395L,
//                -1981500378889374518L, -8219243506348948437L, 5146132043411957838L, 6242824757159542603L, -2527691444947296299L, -7829257710384305046L, -3979136175127034588L, -4114460505497727855L, 613711846571433146L);

        for (Integer iterations : maxIterationOptions) {
            trainWithBackpropagation(iterations);
        }
        System.exit(0);


        System.out.println("BACKPROP " + System.nanoTime());

        System.out.println("RHC " + System.nanoTime());
        trainFeedForwardWithOptimizationAlgorithm(nno -> new RandomizedHillClimbing(nno), 10000);
        System.out.println("GA " + System.nanoTime());

        final List<Integer> populationSizes = Arrays.asList(400, 4000);
        final List<Integer> mateFactors = Arrays.asList(100, 200);
        final List<Integer> mutateFactors = Arrays.asList(100, 200);

        trainFeedForwardWithOptimizationAlgorithm(nno -> new StandardGeneticAlgorithm(4000, 200, 200, nno), 10000);
        System.out.println("SA " + System.nanoTime());

        final List<Double> coolingFactors = Arrays.asList(0.30, 0.60, 0.95);
        final List<Double> temperatureFactors = Arrays.asList(1e5, 1e10, 1e15);


        trainFeedForwardWithOptimizationAlgorithm(nno -> new SimulatedAnnealing(1e11, 0.95, nno), 10000);
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
        FeedForwardNetwork network = factory.createClassificationNetwork(new int[]{FEATURE_COUNT, 10, 5, 1}, new Rectifier());

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
        OptimizationAlgorithm o = optimizationFactory.apply(nno);
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
        TestMetric cm = new ConfusionMatrixTestMetric(labels);
        Tester t = new BinaryOutputNeuralNetworkTester(network, acc, cm);
        t.test(patterns);

        acc.printResults();
        cm.printResults();
    }

    public static void trainWithBackpropagation(int iterations) {
        int[] labels = new int[]{0, 1};
        BackPropagationNetworkFactory factory =
                new BackPropagationNetworkFactory();

        Instance[] patterns = trainingInstances;
        BackPropagationNetwork network = factory.createClassificationNetwork(
                LAYERS, new Rectifier());
        SumOfSquaresError measure = new SumOfSquaresError();
        DataSet set = new DataSet(patterns);
        BatchBackPropagationTrainer batchBackPropagationTrainer = new BatchBackPropagationTrainer(set, network, measure, new RPROPUpdateRule(0.01, 50, 0.00001));
        ConvergenceTrainer fit = new ConvergenceTrainer(batchBackPropagationTrainer, iterations);
        long timeTook = benchmarkMillis(fit);

        // to dump to csv: "iterations", "algorithmWithParams", "errorCurve", "true-positive", "true-negative", "false-positive", "false-negative", "time"



        double[] weights = network.getWeights();
        //network.setWeights(opt.getData());

        //10) Run the training data through the network with the weights discovered through optimization, and
        //    print out the expected label and result of the classifier for each instance.
        TestMetric acc = new AccuracyTestMetric();
        TestMetric cm = new ConfusionMatrixTestMetric(labels);
        Tester t = new BinaryOutputNeuralNetworkTester(network, acc, cm);
        t.test(testInstances);

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
    public static class AlwaysZeroEvaluationFunction implements EvaluationFunction {
        @Override
        public double value(Instance d) {
            return 0;
        }
    }


}
