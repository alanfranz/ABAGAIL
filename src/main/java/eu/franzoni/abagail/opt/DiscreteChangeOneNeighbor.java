package eu.franzoni.abagail.opt;

import eu.franzoni.abagail.shared.MyRandom;
import eu.franzoni.abagail.dist.Distribution;

import eu.franzoni.abagail.shared.Instance;

/**
 * A neighbor function for changing a single value
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DiscreteChangeOneNeighbor implements NeighborFunction {
    
    /**
     * The ranges of the different values
     */
    private int[] ranges;
    
    /**
     * Make a new change one neighbor function
     * @param ranges the ranges of the data
     */
    public DiscreteChangeOneNeighbor(int[] ranges) {
        this.ranges = ranges;
    }
    
    /**
     * @see opt.NeighborFunction#neighbor(opt.OptimizationData)
     */
    public Instance neighbor(Instance d) {
        Instance cod = (Instance) d.copy();
        int i = MyRandom.provideRandom().nextInt(ranges.length);
        cod.getData().set(i, MyRandom.provideRandom().nextInt(ranges[i]));
        return cod;
    }

}