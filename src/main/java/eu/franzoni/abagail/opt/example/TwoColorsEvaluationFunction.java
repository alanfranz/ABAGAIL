package eu.franzoni.abagail.opt.example;

import eu.franzoni.abagail.util.linalg.Vector;
import eu.franzoni.abagail.opt.EvaluationFunction;
import eu.franzoni.abagail.shared.Instance;

/**
 * A function that evaluates whether a vector represents a 2-colored graph
 * @author Daniel Cohen dcohen@gatech.edu
 * @version 1.0
 */
public class TwoColorsEvaluationFunction implements EvaluationFunction {

    /**
     * @see opt.EvaluationFunction#value(opt.OptimizationData)
     */
    public double value(Instance d) {
        Vector data = d.getData();
        double val = 0;
        for (int i = 1; i < data.size() - 1; i++) {
            if ((data.get(i) != data.get(i-1)) && (data.get(i) != data.get(i+1))) {
                val++;
            }
        }

        return val;
    }

}
