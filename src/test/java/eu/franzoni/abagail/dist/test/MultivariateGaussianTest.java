package eu.franzoni.abagail.dist.test;

import eu.franzoni.abagail.dist.MultivariateGaussian;
import eu.franzoni.abagail.shared.DataSet;
import eu.franzoni.abagail.shared.Instance;
import eu.franzoni.abagail.util.linalg.DenseVector;
import eu.franzoni.abagail.util.linalg.RectangularMatrix;

/**
 * Testing
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class MultivariateGaussianTest {
    
    /**
     * The test main
     * @param args ignored
     */
    public static void main(String[] args) {
        Instance[] instances = new Instance[20];
        MultivariateGaussian mga = new MultivariateGaussian(new DenseVector(new double[] {100, 100, 100}), RectangularMatrix.eye(3).times(.01)); 
        for (int i = 0; i < instances.length; i++) {
            instances[i] = mga.sample();
            System.out.println(instances[i]);
        }
    
        DataSet set = new DataSet(instances);
        MultivariateGaussian mg = new MultivariateGaussian();
        mg.estimate(set);
        System.out.println(mg);
        System.out.println("Most likely " + mg.mode(null));
        for (int i = 0; i < 10; i++) {
            System.out.println(mg.sample(null));
        }
        for (int i = 0; i < instances.length; i++) {
            System.out.println("Probability of \n" + instances[i] 
                + "\n " + mg.p(instances[i]));
        }
    }

}
