package eu.franzoni.abagail.opt.example;

import eu.franzoni.abagail.opt.MaximizableEvaluationFunction;
import eu.franzoni.abagail.util.linalg.DenseVector;
import eu.franzoni.abagail.util.linalg.Vector;
import eu.franzoni.abagail.opt.EvaluationFunction;
import eu.franzoni.abagail.shared.Instance;

import java.util.Arrays;

/**
 * A four peaks evaluation function
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class FourPeaksEvaluationFunction implements MaximizableEvaluationFunction {
    /**
     * The t value
     */
    private int t;
    
    /**
     * Make a new four peaks function
     * @param t the t value
     */
    public FourPeaksEvaluationFunction(int t) {
        this.t = t;
    }

    /**
     * @see opt.EvaluationFunction#value(opt.OptimizationData)
     */
    public double value(Instance d) {
        Vector data = d.getData();
        int i = 0;
        while (i < data.size() && data.get(i) == 1) {
            i++;
        }
        int head = i;

        i = data.size() - 1;
        while (i >= 0 && data.get(i) == 0) {
            i--;
        }
        int tail = data.size() - 1 - i;

        int r = 0;
        if (head > t && tail > t) {
            r = data.size();
        }
        return Math.max(tail, head) + r;
    }

    public static int tail(Vector data) {
        int i = data.size() - 1;
        while (i >= 0 && data.get(i) == 0) {
            i--;
        }
        return data.size() - 1 - i;
    }

    // don't know how it works with odd lengths. We don't care.
    public double findTheoreticalMaximum(final int dataSize) {
        final double[] myData = new double[dataSize];
        Arrays.fill(myData, 0.0);
        Arrays.fill(myData, 0, this.t+1, 1.0);
        return this.value(new Instance(new DenseVector(myData)));
    }

    public static void main(String[] args) {
        System.out.println(tail(new DenseVector(new double[]{0.0,0.0,0.0,0.0})));
    }
    
    
}
