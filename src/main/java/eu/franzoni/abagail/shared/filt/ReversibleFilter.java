package eu.franzoni.abagail.shared.filt;

import eu.franzoni.abagail.shared.DataSet;

/**
 * A reversible filter is a filter that can be undone
 * @author Andrew Guillory gtg008g@mail.gatech.edu
 * @version 1.0
 */
public interface ReversibleFilter extends DataSetFilter {
    /**
     * Perform the reverse on the given data set
     * @param set the set to reverse
     */
    public void reverse(DataSet set);

}
