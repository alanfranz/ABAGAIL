package eu.franzoni.abagail.rl;

import eu.franzoni.abagail.shared.MyRandom;
import eu.franzoni.abagail.dist.Distribution;

/**
 * An epsilon greedy exploration strategy
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class EpsilonGreedyStrategy implements ExplorationStrategy {
    /**
     * The epsilon value
     */
    private double epsilon;
    
    /**
     * Make a epsilon greedy strategy
     * @param epsilon the epsilon value
     */
    public EpsilonGreedyStrategy(double epsilon) {
        this.epsilon = epsilon;
    }

    /**
     * @see rl.ExplorationStrategy#action(double[])
     */
    public int action(double[] qvalues) {
        if (MyRandom.provideRandom().nextDouble() < epsilon) {
            return MyRandom.provideRandom().nextInt(qvalues.length);
        }
        int best = 0;
        for (int i = 1; i < qvalues.length; i++) {
            if (qvalues[best] < qvalues[i]) {
                best = i;
            }
        }
        return best;
    }

}
