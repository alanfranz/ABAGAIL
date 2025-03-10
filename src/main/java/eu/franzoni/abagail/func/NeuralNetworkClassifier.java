package eu.franzoni.abagail.func;

import eu.franzoni.abagail.dist.*;
import eu.franzoni.abagail.shared.MyRandom;
import eu.franzoni.abagail.dist.Distribution;
import eu.franzoni.abagail.dist.DiscreteDistribution;
import eu.franzoni.abagail.shared.ConvergenceTrainer;
import eu.franzoni.abagail.shared.DataSet;
import eu.franzoni.abagail.shared.DataSetDescription;
import eu.franzoni.abagail.shared.GradientErrorMeasure;
import eu.franzoni.abagail.shared.Instance;
import eu.franzoni.abagail.shared.SumOfSquaresError;
import eu.franzoni.abagail.shared.Trainer;
import eu.franzoni.abagail.func.nn.activation.DifferentiableActivationFunction;
import eu.franzoni.abagail.func.nn.activation.HyperbolicTangentSigmoid;
import eu.franzoni.abagail.func.nn.backprop.BackPropagationNetwork;
import eu.franzoni.abagail.func.nn.backprop.BackPropagationNetworkFactory;
import eu.franzoni.abagail.func.nn.backprop.BatchBackPropagationTrainer;
import eu.franzoni.abagail.func.nn.backprop.RPROPUpdateRule;
import eu.franzoni.abagail.func.nn.backprop.WeightUpdateRule;

/**
 * A neural network classifier
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class NeuralNetworkClassifier extends AbstractConditionalDistribution implements FunctionApproximater {
    
    /**
     * The transfer function
     */
    private DifferentiableActivationFunction activationFunction;
    
    /**
     * The hidden node count
     */
    private int hiddenNodeCount;

    /**
     * The training weight update rule
     */
    private WeightUpdateRule updateRule;
    
    /**
     * The network
     */
    private BackPropagationNetwork network;
    
    /**
     * Make a new nn classifier
     * @param hiddenNodeCount the hidden node count
     * @param activationFunction the activation function
     * @param updateRule the update rule
     */
    public NeuralNetworkClassifier(int hiddenNodeCount,
            DifferentiableActivationFunction activationFunction,
            WeightUpdateRule updateRule) {
        this.hiddenNodeCount = hiddenNodeCount;
        this.activationFunction = activationFunction;
        this.updateRule = updateRule;             
    }
    
    /**
     * Make a new classifier
     */
    public NeuralNetworkClassifier() {
        this(3, new HyperbolicTangentSigmoid(), new RPROPUpdateRule());
    }

    /**
     * @see func.FunctionApproximater#estimate(shared.DataSet)
     */
    public void estimate(DataSet set) {
        if (set.getDescription() == null) {
            set.setDescription(new DataSetDescription(set));
        }
        int[] topology;
        if (hiddenNodeCount != 0) {
            topology = new int[3];
            topology[1] = hiddenNodeCount;
        } else {
            topology = new int[2];
        }
        topology[0] = set.getDescription().getAttributeTypes().length;
        if (set.getDescription().getLabelDescription().getDiscreteRange() == 2) {
            topology[topology.length - 1] = 1;
        } else {
            topology[topology.length - 1] = 
                set.getDescription().getLabelDescription().getDiscreteRange();
        }
        network = (new BackPropagationNetworkFactory())
            .createClassificationNetwork(topology, activationFunction);
        GradientErrorMeasure errorMeasure = new SumOfSquaresError();
        Trainer trainer = new ConvergenceTrainer(
            new BatchBackPropagationTrainer(
                set, network, errorMeasure, updateRule));
        trainer.train();
    }
    
    /**
     * Get a distribution for the given input
     * @param input the input
     * @return the distribution
     */
    public Distribution distributionFor(Instance input) {
        network.setInputValues(input.getData());
        network.run();
        if (network.getOutputLayer().getNodeCount() > 1) {
            return new DiscreteDistribution(
                network.getOutputValues());            
        } else {
            double[] p = new double[2];
            p[1] = network.getOutputValues().get(0);
            p[0] = 1 - p[1];
            return new DiscreteDistribution(
                p);
        }
    }

    /**
     * @see func.FunctionApproximater#value(shared.Instance)
     */
    public Instance value(Instance i) {
        return distributionFor(i).mode();
    }
}
