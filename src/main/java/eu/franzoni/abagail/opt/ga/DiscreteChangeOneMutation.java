package eu.franzoni.abagail.opt.ga;

import eu.franzoni.abagail.shared.MyRandom;
import eu.franzoni.abagail.dist.Distribution;

import eu.franzoni.abagail.shared.Instance;

/**
 * A mutation function for changing a single value
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DiscreteChangeOneMutation implements MutationFunction {    
    /**
     * The ranges of the different values
     */
    private int[] ranges;
    
    /**
     * Make a new discrete change one mutation function
     * @param ranges the ranges of the data
     */
    public DiscreteChangeOneMutation(int[] ranges) { 
        this.ranges = ranges;
    }

    /**
     * @see opt.ga.MutationFunction#mutate(opt.OptimizationData)
     */
    public void mutate(Instance d) {
        int i = MyRandom.provideRandom().nextInt(d.size());
        d.getData().set(i, MyRandom.provideRandom().nextInt(ranges[i]));
    }

}
