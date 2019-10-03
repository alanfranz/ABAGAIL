package eu.franzoni.abagail.opt;

import eu.franzoni.abagail.shared.MyRandom;
import eu.franzoni.abagail.dist.Distribution;

import eu.franzoni.abagail.shared.Instance;

/**
 * A continuous add one neighbor function
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class ContinuousAddOneNeighbor implements NeighborFunction {
    /**
     * The amount to add to the value
     */
    private double amount;
    
    /**
     * Continuous add one neighbor
     * @param amount the amount to add
     */
    public ContinuousAddOneNeighbor(double amount) {
        this.amount = amount;
    }
    
    /**
     * Continuous add one neighbor
     */
    public ContinuousAddOneNeighbor() {
        this(1);
    }

    /**
     * @see opt.NeighborFunction#neighbor(opt.OptimizationData)
     */
    public Instance neighbor(Instance d) {
        int i = MyRandom.provideRandom().nextInt(d.size());
        Instance cod = (Instance) d.copy();
        cod.getData().set(i, cod.getContinuous(i)+ MyRandom.provideRandom().nextDouble() * amount - amount / 2);
        return cod;
    }
}
