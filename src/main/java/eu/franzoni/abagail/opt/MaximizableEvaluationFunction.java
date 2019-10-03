package eu.franzoni.abagail.opt;

import eu.franzoni.abagail.opt.EvaluationFunction;

public interface MaximizableEvaluationFunction extends EvaluationFunction {

    public double findTheoreticalMaximum(final int dataSize);

}
