package eu.franzoni.abagail.opt.prob;

import eu.franzoni.abagail.shared.MyRandom;
import eu.franzoni.abagail.dist.Distribution;
import eu.franzoni.abagail.opt.OptimizationProblem;

/**
 * An optimization problem solvable by MIMIC
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface ProbabilisticOptimizationProblem extends OptimizationProblem {
    
    /**
     * Build a distribution from the given data
     * @param data the data to build the distribution from
     * @return the distrubtion
     */
    public abstract Distribution getDistribution();

}
