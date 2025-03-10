package eu.franzoni.abagail.dist.test;

import eu.franzoni.abagail.util.linalg.DenseVector;
import eu.franzoni.abagail.util.linalg.RectangularMatrix;
import eu.franzoni.abagail.shared.MyRandom;
import eu.franzoni.abagail.dist.Distribution;
import eu.franzoni.abagail.dist.MixtureDistribution;
import eu.franzoni.abagail.dist.DiscreteDistribution;
import eu.franzoni.abagail.dist.MultivariateGaussian;
import eu.franzoni.abagail.shared.DataSet;
import eu.franzoni.abagail.shared.Instance;

/**
 * Testing
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class MixtureDistributionTest {
    
    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) throws Exception {
        Instance[] instances = new Instance[100];
        MultivariateGaussian mga = new MultivariateGaussian(new DenseVector(new double[] {100, 100, 100}), RectangularMatrix.eye(3).times(.01)); 
        MultivariateGaussian mgb = new MultivariateGaussian(new DenseVector(new double[] {-1, -1, -1}), RectangularMatrix.eye(3).times(10)); 
        for (int i = 0; i < instances.length; i++) {
            if (MyRandom.provideRandom().nextBoolean()) {
                instances[i] = mga.sample();   
            } else {
                instances[i] = mgb.sample();
            }
            System.out.println(instances[i]);
        }
        DataSet set = new DataSet(instances);
        MixtureDistribution md = new MixtureDistribution(new Distribution[] {
            new MultivariateGaussian(new DenseVector(new double[] {120, 80, 100}), RectangularMatrix.eye(3).times(1)),
            new MultivariateGaussian(new DenseVector(new double[] {-1, -6, -5}), RectangularMatrix.eye(3).times(1))},
            DiscreteDistribution.random(2).getProbabilities());
        System.out.println(md);
        for (int i = 0; i < 20; i++) {
            md.estimate(set);
            System.out.println(md);
        }
        System.out.println(md);
        for (int i = 0; i < 30; i++) {
            System.out.println(md.sample(null));
        }
    }

}
