package eu.franzoni.abagail.shared;

public class TimeBoundedTrainer implements Trainer {

    private Trainer trainer;
    private int maxSeconds;
    private double[] errorCurve;
    private long iterations = 0L;


    public TimeBoundedTrainer(Trainer t, int maxSeconds) {
        this.trainer = t;
        this.maxSeconds = maxSeconds;
        this.errorCurve = new double[maxSeconds];
    }

    public double train() {
        final long startTime = System.currentTimeMillis();
        final long endTime = startTime + 1000 * maxSeconds;
        double sum = 0;

        long latestErrorCurveAt = Long.MIN_VALUE;
        long currentTime;
        while ((currentTime = System.currentTimeMillis()) < endTime) {
            final double error = trainer.train();
            sum += error;
            iterations++;
            if ((currentTime - latestErrorCurveAt) > 1000) {
                long index = (currentTime - startTime) / 1000;
                this.errorCurve[(int)index] = error;
                latestErrorCurveAt = currentTime;
            }
        }
        return sum / iterations;
    }

    public double[] getErrorCurve() {
        return errorCurve;
    }


}
