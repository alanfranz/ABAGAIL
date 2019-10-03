package eu.franzoni.abagail.opt.example;

import eu.franzoni.abagail.opt.EvaluationFunction;
import eu.franzoni.abagail.opt.MaximizableEvaluationFunction;
import eu.franzoni.abagail.shared.Instance;
import eu.franzoni.abagail.util.linalg.DenseVector;
import eu.franzoni.abagail.util.linalg.Vector;

import java.util.Arrays;

public class SixPeaksEvaluationFunction implements MaximizableEvaluationFunction {
    private int t;

    public SixPeaksEvaluationFunction(int t) {
        this.t = t;
    }

    public double value(Instance d) {
        Vector data = d.getData();

        int i = 0;
        while (i < data.size() && data.get(i) == 1) {
            i++;
        }
        final int countHead1 = i;


        i = 0;
        while (i < data.size() && data.get(i) == 0) {
            i++;
        }
        final int countHead0 = i;


        final int lastIndex = data.size() - 1;
        i = lastIndex;
        while (i >= 0 && data.get(i) == 0) {
            // i will get to -1 if everything is at 0
            i--;
        }
        final int countTail0 = lastIndex - i;

        i = lastIndex;
        while (i >= 0 && data.get(i) == 1) {
            // i will get to -1 if everything is at 0
            i--;
        }
        final int countTail1 = lastIndex - i;

        int r = 0;
        if ((countHead1 > t && countTail0 > t) || (countTail1 > t && countHead0 > t)) {
            r = data.size();
        }
        return Math.max(countTail0, countHead1) + r;
    }

    // don't know how it works with odd lengths. We don't care.
    public double findTheoreticalMaximum(final int dataSize) {
        final double[] myData = new double[dataSize];
        Arrays.fill(myData, 0.0);
        Arrays.fill(myData, 0, this.t+1, 1.0);
        return this.value(new Instance(new DenseVector(myData)));
    }
}
