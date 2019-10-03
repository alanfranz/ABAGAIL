package eu.franzoni.abagail.shared.writer;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Write arbitrary data to a CSV file.  This is used to write results out,
 * to be consumed by another program (GNUPlot, etc).
 * 
 * @author Jesse Rosalia <https://github.com/theJenix>
 * @date 2013-03-07
 *
 */
public class CSVWriter implements Writer {

    private String fileName;
    private List<String> fields;
    private List<String> buffer;
    private FileWriter fileWriter;

    public CSVWriter(String fileName, String[] fields) {
        this.fileName = fileName;
        this.fields   = Arrays.asList(fields);
        this.buffer   = new ArrayList<String>();
        this.open();

    }

    @Override
    public void close(){
        try {
            this.fileWriter.close();
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private void open()  {
        try {
            this.fileWriter = new FileWriter(fileName);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
        writeRow(this.fields);
    }

    /**
     * @param toWrite
     * @throws IOException
     */
    private void writeRow(List<String> toWrite) {
        try {
            boolean addComma = false;
            for (String field : toWrite) {
                if (addComma) {
                    this.fileWriter.append(",");
                }
                this.fileWriter.append(field);
                addComma = true;
            }
            this.fileWriter.append('\n');
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    @Override
    public void write(String str)  {
        if (str.contains(",")) {
            throw new IllegalArgumentException("unsupported input comma");
        }
        this.buffer.add(str);
    }

    public void writeMany(String... strings) {
        for (String str: strings) {
            if (str.contains(",")) {
                throw new IllegalArgumentException("unsupported input comma");
            }
            this.buffer.add(str);
        }

        this.nextRecord();
    }

    @Override
    public void nextRecord() {
        if (buffer.size() != this.fields.size()) {
            throw new IllegalStateException("incomplete record");
        }
        writeRow(buffer);
        //clear the buffer for the next record
        buffer.clear();
    }
}
