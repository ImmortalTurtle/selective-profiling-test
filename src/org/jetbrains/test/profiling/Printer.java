package org.jetbrains.test.profiling;

import javenue.csv.Csv;

import java.io.*;

/**
 * * Purpose of this class is to open file
 * and write {@link FullCallRecords} into it,
 * which can be done by serializing it or
 * by carefully printing in a human-readable form.
 *
 * @author Egor Nemchinov
 */
public class Printer {

    /**
     * Method serializes and prints FullCallRecords into file.
     * @param callRecords FullCallRecords to save into file.
     * @param filePath Relative to project dir(like "src/...")
     *                or absolute path("/usr/...")
     * @throws IOException WHen there are problems with output file.
     */
    public static void printSerialized(FullCallRecords callRecords, String filePath) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(FileUtil.getFileByPath(filePath))){
            ObjectOutputStream stream = new ObjectOutputStream(outputStream);
            stream.writeObject(callRecords);
            stream.flush();
            outputStream.close();
        }
    }

    /**
     * Method generates CSV file, which contains information
     * in a human-readable form. Each call tree's level is represented by a column.
     *
     * @param callRecords FullCallRecords to save into file.
     * @param filePath Relative to project dir(like "src/...")
     *                or absolute path("/usr/...")
     * @param delimeter Character to separate values. Usually either ',' or ';'.
     *                  Though ',' is recommended.
     * @throws Csv.IOException When there are problems with output file
     */
    public static void printCsv(FullCallRecords callRecords, String filePath, char delimeter) throws Csv.IOException{
        Csv.Writer writer = new Csv.Writer(FileUtil.getFileByPath(filePath))
                .delimiter(delimeter);
        CallTreeIterator iterator;
        CallData current;
        int columnsAmount = callRecords.calculateMaxLevel() + 1;
        for(ThreadCallTree callTree: callRecords.getCallTreeList()) {
            writer.value("Thread-"+callTree.getIndex()+":");
            repeatEmpty(writer, columnsAmount - 1);
            writer.newLine();
            iterator = new CallTreeIterator(callTree);
            while(iterator.hasNext()) {
                current = iterator.next();
                repeatEmpty(writer, current.getLevel());
                writer.value(current.toString());
                repeatEmpty(writer,columnsAmount - current.getLevel() - 1);
                writer.newLine();
            }
        }
        writer.close();
    }

    private static void repeatEmpty(Csv.Writer writer, int amount) {
        for(int i = 0; i < amount; i++) {
            writer.value("");
        }
    }
}
