package eu.franzoni.abagail.rl;

import eu.franzoni.abagail.shared.Trainer;

/**
 * A policy learner is also a trainer
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface PolicyLearner extends Trainer {
    /**
     * Get the best policy
     * @return the policy
     */
    public Policy getPolicy();

}
