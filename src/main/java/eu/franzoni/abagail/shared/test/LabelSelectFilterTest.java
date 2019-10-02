package eu.franzoni.abagail.shared.test;

import java.io.File;

import eu.franzoni.abagail.shared.DataSet;
import eu.franzoni.abagail.shared.DataSetDescription;
import eu.franzoni.abagail.shared.filt.ContinuousToDiscreteFilter;
import eu.franzoni.abagail.shared.filt.LabelSelectFilter;
import eu.franzoni.abagail.shared.filt.LabelSplitFilter;
import eu.franzoni.abagail.shared.reader.ArffDataSetReader;
import eu.franzoni.abagail.shared.reader.DataSetLabelBinarySeperator;
import eu.franzoni.abagail.shared.reader.DataSetReader;

public class LabelSelectFilterTest {
    /**
     * The test main
     * @param args ignored parameters
     */
    public static void main(String[] args) throws Exception {
        DataSetReader dsr = new ArffDataSetReader(new File("").getAbsolutePath() + "/src/shared/test/abalone.arff");
        // read in the raw data
        DataSet ds = dsr.read();
        // split out the label
        LabelSelectFilter lsf = new LabelSelectFilter(1);
        lsf.filter(ds);
        ContinuousToDiscreteFilter ctdf = new ContinuousToDiscreteFilter(10);
        ctdf.filter(ds);
        System.out.println(ds);
        System.out.println(new DataSetDescription(ds));
    }
}
