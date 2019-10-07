package eu.franzoni.abagail.shared;

/**
 * A fixed iteration trainer
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class FixedIterationTrainer implements Trainer {
    
    /**
     * The inner trainer
     */
    private Trainer trainer;
    
    /**
     * The number of iterations to train
     */
    private int iterations;
    private double[] errorCurve;
    private double[] cumulativeErrorCurve;
    
    /**
     * Make a new fixed iterations trainer
     * @param t the trainer
     * @param iter the number of iterations
     */
    public FixedIterationTrainer(Trainer t, int iter) {
        trainer = t;
        iterations = iter;
        this.errorCurve = new double[iter];
        this.cumulativeErrorCurve = new double[iter];
    }

    /**
     * @see shared.Trainer#train()
     */
    public double train() {
        double sum = 0;
        for (int i = 0; i < iterations; i++) {
            double train = trainer.train();
            sum += train;
            this.errorCurve[i] = train;
            this.cumulativeErrorCurve[i] = sum/(i+1);
        }
        return sum / iterations;
    }

    public double[] getErrorCurve() {
        return errorCurve;
    }

    public double[] getCumulativeErrorCurve() {
        return cumulativeErrorCurve;
    }
}
