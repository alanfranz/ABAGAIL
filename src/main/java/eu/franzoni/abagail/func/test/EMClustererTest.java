package eu.franzoni.abagail.func.test;

import eu.franzoni.abagail.shared.MyRandom;
import eu.franzoni.abagail.dist.Distribution;
import eu.franzoni.abagail.dist.MultivariateGaussian;
import eu.franzoni.abagail.func.EMClusterer;
import eu.franzoni.abagail.shared.DataSet;
import eu.franzoni.abagail.shared.Instance;
import eu.franzoni.abagail.util.linalg.DenseVector;
import eu.franzoni.abagail.util.linalg.RectangularMatrix;

/**
 * Testing
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class EMClustererTest {
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
                instances[i] = mga.sample(null);   
            } else {
                instances[i] = mgb.sample(null);
            }
        }
        DataSet set = new DataSet(instances);
        EMClusterer em = new EMClusterer();
        em.estimate(set);
        System.out.println(em);
    }
}
