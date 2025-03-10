package eu.franzoni.abagail.shared.filt;

import eu.franzoni.abagail.shared.MyRandom;
import eu.franzoni.abagail.dist.Distribution;

import eu.franzoni.abagail.shared.DataSet;
import eu.franzoni.abagail.shared.Instance;

/**
 * A filter for randomizing the order of a data set
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public class RandomOrderFilter implements DataSetFilter {

    /**
     * @see shared.DataSetFilter#filter(shared.DataSet)
     */
    public void filter(DataSet dataSet) {
        for (int i = dataSet.size()-1; i > 0; i--) {
            int j = MyRandom.provideRandom().nextInt(i + 1);
            Instance temp = dataSet.get(i);
            dataSet.set(i, dataSet.get(j));
            dataSet.set(j, temp);
        }
    }

}
