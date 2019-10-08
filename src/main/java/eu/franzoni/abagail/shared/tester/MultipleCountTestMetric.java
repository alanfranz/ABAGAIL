package eu.franzoni.abagail.shared.tester;

import eu.franzoni.abagail.shared.Instance;
import eu.franzoni.abagail.shared.writer.CSVWriter;

/**
 * A test metric for accuracy.  This metric reports of % correct and % incorrect for a test run.
 * 
 * @author Jesse Rosalia <https://github.com/theJenix>
 * @date 2013-03-05
 */
public class MultipleCountTestMetric extends TestMetric {

    private int count;    
    private int countCorrect;
    private int countTruePositives = 0;
    private int countTrueNegatives = 0;
    private int countFalsePositives = 0;
    private int countFalseNegatives = 0;
    
    @Override
    public void addResult(Instance expected, Instance actual) {
        Comparison c = new Comparison(expected, actual);

        count++;
        if (c.isAllCorrect()) {
            countCorrect++;
            if (expected.getDiscrete() == 1) {
                this.countTruePositives += 1;
            } else {
                this.countTrueNegatives += 1;
            }
        } else {
            if (actual.getDiscrete() == 1) {
                this.countFalsePositives += 1;
            } else {
                this.countFalseNegatives += 1;
            }
        }
    }
    
    public double getPctCorrect() {
        return count > 0 ? ((double)countCorrect)/count : 1; //if count is 0, we consider it all correct
    }

    public void printResults() {
        //only report results if there were any results to report.
        if (count > 0) {
            double pctCorrect = getPctCorrect();
            double pctIncorrect = (1 - pctCorrect);
            System.out.println(String.format("Correctly Classified Instances: %.02f%%",   100 * pctCorrect));
            System.out.println(String.format("Incorrectly Classified Instances: %.02f%%", 100 * pctIncorrect));
            System.out.println(String.format("True positives: %d",  this.countTruePositives));
            System.out.println(String.format("True negatives: %d",  this.countTrueNegatives));
            System.out.println(String.format("False positives: %d",  this.countFalsePositives));
            System.out.println(String.format("False negatives: %d",  this.countFalseNegatives));

        } else {

            System.out.println("No results added.");
        }
    }

    public void writeResults(CSVWriter writer) {
        this.printResults();
        writer.write(Integer.toString(this.countTruePositives));
        writer.write(Integer.toString(this.countTrueNegatives));
        writer.write(Integer.toString(this.countFalsePositives));
        writer.write(Integer.toString(this.countFalseNegatives));
    }


    public int getCountTruePositives() {
        return countTruePositives;
    }

    public int getCountTrueNegatives() {
        return countTrueNegatives;
    }

    public int getCountFalsePositives() {
        return countFalsePositives;
    }

    public int getCountFalseNegatives() {
        return countFalseNegatives;
    }
}
