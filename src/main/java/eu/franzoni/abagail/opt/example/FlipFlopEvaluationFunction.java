package eu.franzoni.abagail.opt.example;

import eu.franzoni.abagail.opt.MaximizableEvaluationFunction;
import eu.franzoni.abagail.util.linalg.DenseVector;
import eu.franzoni.abagail.util.linalg.Vector;
import eu.franzoni.abagail.opt.EvaluationFunction;
import eu.franzoni.abagail.shared.Instance;

/**
 * A function that counts the ones in the data
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class FlipFlopEvaluationFunction implements MaximizableEvaluationFunction {

    public double value(Instance d) {
        Vector data = d.getData();
        double val = 0;
        for (int i = 0; i < data.size() - 1; i++) {
            if (data.get(i) != data.get(i + 1)) {
                val++;
            }
        }
        return val;
    }

    @Override
    public double findTheoreticalMaximum(int dataSize) {
        return dataSize - 1;
    }

    public static void main(String[] args) {
        FlipFlopEvaluationFunction flipFlopEvaluationFunction = new FlipFlopEvaluationFunction();
        System.out.println(flipFlopEvaluationFunction.value(new Instance(new DenseVector(new double[]{1}))));
        System.out.println(flipFlopEvaluationFunction.value(new Instance(new DenseVector(new double[]{1, 1}))));
        System.out.println(flipFlopEvaluationFunction.value(new Instance(new DenseVector(new double[]{1, 1, 0}))));
        System.out.println(flipFlopEvaluationFunction.value(new Instance(new DenseVector(new double[]{1, 0, 1}))));
        System.out.println(flipFlopEvaluationFunction.value(new Instance(new DenseVector(new double[]{0, 1, 0, 1, 0}))));

    }
}
