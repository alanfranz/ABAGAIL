package eu.franzoni.abagail.opt.example;

import eu.franzoni.abagail.opt.MaximizableEvaluationFunction;
import eu.franzoni.abagail.util.linalg.Vector;
import eu.franzoni.abagail.opt.EvaluationFunction;
import eu.franzoni.abagail.shared.Instance;

/**
 * A function that counts the ones in the data
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class CountOnesEvaluationFunction implements MaximizableEvaluationFunction {
    /**
     * @see opt.EvaluationFunction#value(opt.OptimizationData)
     */
    public double value(Instance d) {
        Vector data = d.getData();
        double val = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == 1) {
                val++;
            }
        }
        return val;
    }

    @Override
    public double findTheoreticalMaximum(int dataSize) {
        return dataSize;
    }
}