package eu.franzoni.abagail.dist;

import eu.franzoni.abagail.shared.Instance;
import eu.franzoni.abagail.shared.MyRandom;

import java.util.Random;

/**
 * An abstract distribution
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public abstract class AbstractDistribution implements Distribution {
    protected final Random random = MyRandom.provideRandom();
    
    /**
     * @see dist.Distribution#logp(shared.Instance)
     */
    public double logp(Instance i) {
        double p = p(i);
        double logp = Math.log(p);
        if (Double.isInfinite(logp)) {
            return -Double.MAX_VALUE;
        }
        return logp;
    }
    
    /**
     * Get an unconditional sample
     * @return the unconditional sample
     */
    public Instance sample() {
        return sample(null);
    }
    
    /**
     * Get an unconditional sample
     * @return the unconditional sample
     */
    public Instance mode() {
        return mode(null);
    }

}
