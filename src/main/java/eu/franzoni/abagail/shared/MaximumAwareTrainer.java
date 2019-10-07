package eu.franzoni.abagail.shared;

import eu.franzoni.abagail.opt.EvaluationFunction;
import eu.franzoni.abagail.opt.OptimizationAlgorithm;

import java.util.Arrays;

public class MaximumAwareTrainer implements Trainer {
    public static final double EPSILON = 0.0001;

    /**
     * The inner trainer
     */
    private final OptimizationAlgorithm optimizationAlgorithm;
    private final EvaluationFunction ef;
    private Instance latestEvaluationResult;
    private Integer iterationCountAtLatestEvaluation;
    private final double[] learningCurve;

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
        this.learningCurve = new double[iter];

    }

    public double train() {
        double sum = 0;
        for (int i = 0; i < maxIterations; i++) {
            sum += optimizationAlgorithm.train();
            final Instance currentValue = optimizationAlgorithm.getOptimal();
            final double evaluated = ef.value(currentValue);
            this.learningCurve[i] = evaluated;
            this.latestEvaluationResult = currentValue;
            if (Math.abs(evaluated - maximumExpectedValue) < EPSILON) {
                System.out.println("Early bailout: " + ef.value(currentValue));
                iterationCountAtLatestEvaluation = i+1;
                Arrays.fill(learningCurve, i, maxIterations-1, evaluated);
                return sum / (i+1);
            };
        }
        iterationCountAtLatestEvaluation = maxIterations;
        return sum / maxIterations;
    }

    public Instance getLatestEvaluationResult() {
        return latestEvaluationResult;
    }

    public Integer getIterationCountAtLatestEvaluation() {
        return iterationCountAtLatestEvaluation;
    }
    public double[] getLearningCurve() {
        return learningCurve;
    }
}
