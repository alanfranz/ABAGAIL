package eu.franzoni.abagail.opt;

import eu.franzoni.abagail.shared.MyRandom;
import eu.franzoni.abagail.dist.Distribution;

import eu.franzoni.abagail.shared.Instance;

/**
 * A swap one neighbor function
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class SwapNeighbor implements NeighborFunction {
    
    /**
     * @see opt.ga.MutationFunction#mutate(opt.OptimizationData)
     */
    public Instance neighbor(Instance d) {
        Instance cod = (Instance) d.copy();
        int i = MyRandom.provideRandom().nextInt(cod.getData().size());
        int j = MyRandom.provideRandom().nextInt(cod.getData().size());
        double temp = cod.getContinuous(i);
        cod.getData().set(i, cod.getContinuous(j));
        cod.getData().set(j, temp);
        return cod;
    }
}