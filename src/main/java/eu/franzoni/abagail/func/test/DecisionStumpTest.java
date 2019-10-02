package eu.franzoni.abagail.func.test;

import eu.franzoni.abagail.shared.DataSet;
import eu.franzoni.abagail.shared.Instance;
import eu.franzoni.abagail.func.DecisionStumpClassifier;
import eu.franzoni.abagail.func.dtree.ChiSquarePruningCriteria;
import eu.franzoni.abagail.func.dtree.GINISplitEvaluator;
import eu.franzoni.abagail.func.dtree.InformationGainSplitEvaluator;
import eu.franzoni.abagail.func.dtree.PruningCriteria;
import eu.franzoni.abagail.func.dtree.SplitEvaluator;

/**
 * Test the class
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class DecisionStumpTest {
    
    /**
     * Test main
     * @param args ignored
     */
    public static void main(String[] args) {
        Instance[] instances =  {
            new Instance(new double[] {0, 0, 0, 1}, 1),
            new Instance(new double[] {1, 0, 0, 0}, 1),
            new Instance(new double[] {1, 0, 0, 0}, 1),
            new Instance(new double[] {1, 0, 0, 0}, 1),
            new Instance(new double[] {1, 0, 0, 1}, 0),
            new Instance(new double[] {1, 0, 0, 1}, 0),
            new Instance(new double[] {1, 0, 0, 1}, 0),
            new Instance(new double[] {1, 0, 0, 1}, 0)
        };
        Instance[] tests =  {
            new Instance(new double[] {0, 1, 1, 1}),
            new Instance(new double[] {0, 0, 0, 0}),
            new Instance(new double[] {1, 0, 0, 0}),
            new Instance(new double[] {1, 1, 1, 1})
        };
        DataSet set = new DataSet(instances);
        PruningCriteria cspc = new ChiSquarePruningCriteria(0);
        SplitEvaluator gse = new GINISplitEvaluator();
        SplitEvaluator igse = new InformationGainSplitEvaluator();
        DecisionStumpClassifier ds = new DecisionStumpClassifier(igse);
        ds.estimate(set);
        System.out.println(ds);
        for (int i = 0; i < tests.length; i++) {
            System.out.println(ds.value(tests[i]));
        }
    }
}