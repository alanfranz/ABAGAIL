package eu.franzoni.abagail.func.test;

import eu.franzoni.abagail.shared.DataSet;
import eu.franzoni.abagail.shared.Instance;
import eu.franzoni.abagail.func.GaussianProcessRegression;
import eu.franzoni.abagail.func.svm.LinearKernel;

/**
 * Test the class
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class GaussianProcessRegressionTest {
    
    /**
     * Test main
     * @param args ignored
     */
    public static void main(String[] args) {
        Instance[] instances =  {
            new Instance(new double[] {1}, -1),
            new Instance(new double[] {1}, -1),
            new Instance(new double[] {1}, -1),
            new Instance(new double[] {1}, -1),
            new Instance(new double[] {-1}, 1),
            new Instance(new double[] {-1}, 1),
            new Instance(new double[] {-1}, 1),
            new Instance(new double[] {-1}, 1)
        };
        Instance[] tests =  {
            new Instance(new double[] {-1}),
            new Instance(new double[] {-1}),
            new Instance(new double[] {1}),
            new Instance(new double[] {1})
        };
        DataSet set = new DataSet(instances);
        GaussianProcessRegression gp = new GaussianProcessRegression(
           new LinearKernel(), .01);
        gp.estimate(set);
        System.out.println(gp);
        for (int i = 0; i < tests.length; i++) {
            System.out.println(gp.value(tests[i]));
        }
    }
}