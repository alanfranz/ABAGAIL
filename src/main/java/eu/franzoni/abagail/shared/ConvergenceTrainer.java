package eu.franzoni.abagail.shared;

import java.util.Arrays;

/**
 * A convergence trainer trains a network
 * until convergence, using another trainer
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class ConvergenceTrainer implements Trainer {
    /** The default threshold */
    private static final double THRESHOLD = 1E-10;
    /** The maxium number of iterations */
    private static final int MAX_ITERATIONS = 500;

    /**
     * The trainer
     */
    private Trainer trainer;

    /**
     * The threshold
     */
    private double threshold;
    
    /**
     * The number of iterations trained
     */
    private int iterations;
    
    /**
     * The maximum number of iterations to use
     */
    private int maxIterations;
    private double[] errorCurve;

    /**
     * Create a new convergence trainer
     * @param trainer the thrainer to use
     * @param threshold the error threshold
     * @param maxIterations the maximum iterations
     */
    public ConvergenceTrainer(Trainer trainer,
            double threshold, int maxIterations) {
        this.trainer = trainer;
        this.threshold = threshold;
        this.maxIterations = maxIterations;
        this.errorCurve = new double[maxIterations];
    }
    

    /**
     * Create a new convergence trainer
     * @param trainer the trainer to use
     */
    public ConvergenceTrainer(Trainer trainer) {
        this(trainer, THRESHOLD, MAX_ITERATIONS);
    }

    public ConvergenceTrainer(Trainer trainer, int maxIterations) {
        this(trainer, THRESHOLD, maxIterations);
    }

    /**
     * @see Trainer#train()
     */
    public double train() {
        double lastError;
        double error = Double.MAX_VALUE;
        do {
           iterations++;
           lastError = error;
           error = trainer.train();
           this.errorCurve[iterations-1] = error;
        } while (Math.abs(error - lastError) > threshold
             && iterations < maxIterations);
        if (iterations < maxIterations) {
            Arrays.fill(errorCurve, iterations-1, maxIterations, error);
        }
        return error;
    }

    public double[] getErrorCurve() {
        return errorCurve;
    }

    /**
     * Get the number of iterations used
     * @return the number of iterations
     */
    public int getIterations() {
        return iterations;
    }
    

}
