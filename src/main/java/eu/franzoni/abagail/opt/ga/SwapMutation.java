package eu.franzoni.abagail.opt.ga;

import eu.franzoni.abagail.shared.MyRandom;
import eu.franzoni.abagail.dist.Distribution;
import eu.franzoni.abagail.shared.Instance;

/**
 * A swap one mutation
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class SwapMutation implements MutationFunction {

    /**
     * @see MutationFunction#mutate(opt.OptimizationData)
     */
    public void mutate(Instance d) {
        int i = MyRandom.provideRandom().nextInt(d.size());
        int j = MyRandom.provideRandom().nextInt(d.size());
        double temp = d.getContinuous(i);
        d.getData().set(i, d.getContinuous(j));
        d.getData().set(j, temp);
    }
}
