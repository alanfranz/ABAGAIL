package eu.franzoni.abagail.shared;

public class KnownMaximumTrainer implements Trainer {
    private final Trainer trainer;
    private final int maxIterations;
    private final double knownMaximum;

    public KnownMaximumTrainer(Trainer trainer, int maxIterations, double knownMaximum) {
        this.trainer = trainer;
        this.maxIterations = maxIterations;
        this.knownMaximum = knownMaximum;
    }

    public double train() {
//        int i = 0;
//
//            double currentScore = trainer.train();
//            if (currentScore >= knownMaximum) {
//                return currentScore;
//            }
//        }
        return 1.0;

    }
}
