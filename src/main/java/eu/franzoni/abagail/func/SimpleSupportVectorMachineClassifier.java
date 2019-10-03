package eu.franzoni.abagail.func;

import eu.franzoni.abagail.dist.*;
import eu.franzoni.abagail.shared.MyRandom;
import eu.franzoni.abagail.dist.Distribution;
import eu.franzoni.abagail.dist.DiscreteDistribution;
import eu.franzoni.abagail.shared.DataSet;
import eu.franzoni.abagail.shared.Instance;
import eu.franzoni.abagail.shared.filt.DiscreteToBinaryFilter;
import eu.franzoni.abagail.func.svm.LinearKernel;
import eu.franzoni.abagail.func.svm.SequentialMinimalOptimization;
import eu.franzoni.abagail.func.svm.SupportVectorMachine;
import eu.franzoni.abagail.func.svm.Kernel;

/**
 * 
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class SimpleSupportVectorMachineClassifier extends AbstractConditionalDistribution implements FunctionApproximater {
    
    /**
     * The svm itself
     */
    private SupportVectorMachine svm;
    
    /**
     * The kernel
     */
    private Kernel kernel;
    
    /**
     * The c value
     */
    private double c;
    
    /**
     * Make a new svm classifier
     */
    public SimpleSupportVectorMachineClassifier() {
        this(1, new LinearKernel());
    }
    
    /**
     * Make a new svm classifier
     * @param c the c value
     * @param kernel the kernel
     */
    public SimpleSupportVectorMachineClassifier(double c, 
            Kernel kernel) {
        this.c = c;
        this.kernel = kernel;            
    }

    /**
     * @see func.FunctionApproximater#estimate(shared.DataSet)
     */
    public void estimate(DataSet set) {
        DiscreteToBinaryFilter dtbf = new DiscreteToBinaryFilter();
        dtbf.filter(set);
        SequentialMinimalOptimization smo = 
            new SequentialMinimalOptimization(set, kernel, c);
	smo.train();
        svm = smo.getSupportVectorMachine();
    }

    /**
     * @see func.FunctionApproximater#value(shared.Instance)
     */
    public Instance value(Instance i) {
        return svm.value(i);
    }
    
    /**
     * @see func.Classifier#classDistribution(shared.Instance)
     */
    public Distribution distributionFor(Instance data) {
        Instance v = value(data);
        double[] p = new double[2];
        p[v.getDiscrete()] = 1;
        return new DiscreteDistribution(p);
    }

    /**
     * Get the c value
     * @return the c value
     */
    public double getC() {
        return c;
    }

    /**
     * Get the kernel
     * @return the kernel
     */
    public Kernel getKernel() {
        return kernel;
    }

    /**
     * Set the c value
     * @param d the new c value
     */
    public void setC(double d) {
        c = d;
    }

    /**
     * Set the kernel function
     * @param kernel the new kernel function
     */
    public void setKernel(Kernel kernel) {
        this.kernel = kernel;
    }

}
