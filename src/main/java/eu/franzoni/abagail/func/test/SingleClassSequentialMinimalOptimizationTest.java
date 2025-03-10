package eu.franzoni.abagail.func.test;

import eu.franzoni.abagail.shared.MyRandom;
import eu.franzoni.abagail.dist.MultivariateGaussian;
import eu.franzoni.abagail.shared.DataSet;
import eu.franzoni.abagail.shared.Instance;
import eu.franzoni.abagail.util.linalg.DenseVector;
import eu.franzoni.abagail.util.linalg.RectangularMatrix;
import eu.franzoni.abagail.func.svm.LinearKernel;
import eu.franzoni.abagail.func.svm.PolynomialKernel;
import eu.franzoni.abagail.func.svm.RBFKernel;
import eu.franzoni.abagail.func.svm.SigmoidKernel;
import eu.franzoni.abagail.func.svm.SingleClassSequentialMinimalOptimization;
import eu.franzoni.abagail.func.svm.SingleClassSupportVectorMachine;

/**
 * A test class
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class SingleClassSequentialMinimalOptimizationTest {
    
    /**
     * Test main
     * @param args ignored
     */
    public static void main(String[] args) {
        Instance[] instances = new Instance[100];
        boolean[] instancesRare = new boolean[100];
        MultivariateGaussian mga = new MultivariateGaussian(new DenseVector(new double[] {100, 100, 100}), RectangularMatrix.eye(3).times(.01)); 
        MultivariateGaussian mgb = new MultivariateGaussian(new DenseVector(new double[] {-1, -1, -1}), RectangularMatrix.eye(3).times(1)); 
        for (int i = 0; i < instances.length; i++) {
            if (MyRandom.provideRandom().nextDouble() < .05) {
                instances[i] = mga.sample(null);
                instancesRare[i] = true;
            } else {
                instances[i] = mgb.sample(null);
            }
        }
        double avgP = 0;
        for (int i = 0; i < instances.length; i++) {
            avgP += mga.p(instances[i]);
        }
        System.out.println("Average p : " + avgP / instances.length);
        PolynomialKernel pk = new PolynomialKernel(2, true);
        LinearKernel lk = new LinearKernel();
        SigmoidKernel sk = new SigmoidKernel();
        RBFKernel rk = new RBFKernel(10);
        SingleClassSequentialMinimalOptimization smo =
            new SingleClassSequentialMinimalOptimization(new DataSet(instances), 
               rk, .1);
        smo.train();
        SingleClassSupportVectorMachine svm = smo.getSupportVectorMachine();
        System.out.println(svm);
        System.out.println("Num iterations " + smo.getNumberOfIterations());
        System.out.println("Num svs " + svm.getSupportVectors().size());
        instances = svm.getSupportVectors().getInstances();
        avgP = 0;
        for (int i = 0; i < instances.length; i++) {
            avgP += mga.p(instances[i]);
        }
        System.out.println("SV Average p: " + avgP / instances.length);
        return;
    }
}
