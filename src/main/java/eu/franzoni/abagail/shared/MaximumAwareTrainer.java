package eu.franzoni.abagail.shared;

import eu.franzoni.abagail.opt.EvaluationFunction;
import eu.franzoni.abagail.opt.OptimizationAlgorithm;

public class MaximumAwareTrainer implements Trainer {
    public static final double EPSILON = 0.0001;

    /**
     * The inner trainer
     */
    private final OptimizationAlgorithm optimizationAlgorithm;
    private final EvaluationFunction ef;
    private Instance actualMaximum;

    /**
     * The number of iterations to train
     */
    private final int maxIterations;
    private final double maximumExpectedValue;

    /**
     * Make a new fixed iterations trainer
     * @param t the trainer
     * @param iter the number of iterations
     */
    public MaximumAwareTrainer(OptimizationAlgorithm t, EvaluationFunction ef, double maximumExpectedValue, int iter) {
        this.optimizationAlgorithm = t;
        this.ef = ef;
        this.maxIterations = iter;
        this.maximumExpectedValue = maximumExpectedValue;
    }

    public double train() {
        double sum = 0;
        for (int i = 0; i < maxIterations; i++) {
            sum += optimizationAlgorithm.train();
            final Instance currentValue = optimizationAlgorithm.getOptimal();
            final double evaluated = ef.value(currentValue);
            if (Math.abs(evaluated - maximumExpectedValue) < EPSILON) {
                this.actualMaximum = currentValue;
                System.out.println("Early bailout: " + ef.value(currentValue));
                return sum / (i+1);
            };
        }
        return sum / maxIterations;
    }

    public Instance getActualMaximum() {
        return actualMaximum;
    }


}
