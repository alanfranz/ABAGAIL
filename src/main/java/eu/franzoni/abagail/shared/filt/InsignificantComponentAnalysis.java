package eu.franzoni.abagail.shared.filt;

import eu.franzoni.abagail.dist.MultivariateGaussian;
import eu.franzoni.abagail.shared.DataSet;
import eu.franzoni.abagail.shared.DataSetDescription;
import eu.franzoni.abagail.shared.Instance;
import eu.franzoni.abagail.util.linalg.Matrix;
import eu.franzoni.abagail.util.linalg.RectangularMatrix;
import eu.franzoni.abagail.util.linalg.SymmetricEigenvalueDecomposition;
import eu.franzoni.abagail.util.linalg.Vector;

/**
 * A filter that performs PCA on a set of data
 * and then keeps the smallest eigenvalues instead of
 * the largest
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class InsignificantComponentAnalysis implements ReversibleFilter {
    /**
     * The default threshold
     */
    private static final double THRESHOLD = Double.MAX_VALUE;
    
    /**
     * The projection matrix
     */
    private Matrix projection;
    
    /**
     * The eigen value matrix
     */
    private Matrix eigenValues;
    
    /**
     * The mean vector
     */
    private Vector mean;
    
    /**
     * Make a new PCA filter
     * @param toKeep the number of components to keep
     * @param dataSet the set form which to estimate components
     */
    public InsignificantComponentAnalysis(DataSet dataSet, int toKeep, double threshold) {
        MultivariateGaussian mg = new MultivariateGaussian();
        mg.estimate(dataSet);
        Matrix covarianceMatrix = mg.getCovarianceMatrix();
        mean = mg.getMean();
        if (toKeep == -1) {
            toKeep = mean.size();
        }
        SymmetricEigenvalueDecomposition sed = 
            new SymmetricEigenvalueDecomposition(covarianceMatrix);
        Matrix eigenVectors = sed.getU();
        eigenValues = sed.getD();
        int belowThreshold = 0;
        while (belowThreshold < toKeep && 
                 eigenValues.get(eigenValues.m() - belowThreshold - 1, 
                     eigenValues.m() - belowThreshold - 1) < threshold) {
            belowThreshold++;
        }
        toKeep = Math.min(toKeep, belowThreshold);
        projection = new RectangularMatrix(toKeep, eigenVectors.m());
        for (int i = eigenVectors.m() - 1; eigenVectors.m() - i  - 1 < toKeep; i--) {
            projection.setRow(eigenVectors.m() - i - 1, eigenVectors.getColumn(i));
        }
    }
    
    public InsignificantComponentAnalysis(DataSet dataSet, double varianceToKeep) {
        MultivariateGaussian mg = new MultivariateGaussian();
        mg.estimate(dataSet);
        Matrix covarianceMatrix = mg.getCovarianceMatrix();
        mean = mg.getMean();

        SymmetricEigenvalueDecomposition sed = 
            new SymmetricEigenvalueDecomposition(covarianceMatrix);
        Matrix eigenVectors = sed.getU();
        eigenValues = sed.getD();

        VarianceCounter vc = new VarianceCounter(eigenValues);
        int toKeep = vc.countRight(varianceToKeep);
        projection = new RectangularMatrix(toKeep, eigenVectors.m());
        for (int i = eigenVectors.m() - 1; eigenVectors.m() - i  - 1 < toKeep; i--) {
            projection.setRow(eigenVectors.m() - i - 1, eigenVectors.getColumn(i));
        }
    }

    /**
     * Make a new PCA filter
     * @param numberOfComponents the number to keep
     * @param set the data set to estimate components from
     */
    public InsignificantComponentAnalysis(DataSet set, int numberOfComponents) {
        this(set, numberOfComponents, THRESHOLD);
    }

    
    /**
     * Make a new PCA filter
     * @param set the data set to estimate components from
     */
    public InsignificantComponentAnalysis(DataSet set) {
        this(set, -1);
    }

    /**
     * @see shared.filt.DataSetFilter#filter(shared.DataSet)
     */
    public void filter(DataSet dataSet) {
        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            instance.setData(instance.getData().minus(mean));
            instance.setData(projection.times(instance.getData()));
        }
        dataSet.setDescription(new DataSetDescription(dataSet));
    }
   

    /**
     * @see shared.filt.ReversibleFilter#reverse(shared.DataSet)
     */
    public void reverse(DataSet dataSet) {
        for (int i = 0; i < dataSet.size(); i++) {
            Instance instance = dataSet.get(i);
            instance.setData(projection.transpose().times(instance.getData()));
            instance.setData(instance.getData().plus(mean));
        }
        dataSet.setDescription(new DataSetDescription(dataSet));
    }

    /**
     * Get the projection matrix used
     * @return the projection matrix
     */
    public Matrix getProjection() {
        return projection;
    }
    
    /**
     * Get the mean
     * @return the mean
     */
    public Vector getMean() {
        return mean;
    }

    /**
     * Get the eigenvalues
     * @return the eigenvalues
     */
    public Matrix getEigenValues() {
        return eigenValues;
    }


}
