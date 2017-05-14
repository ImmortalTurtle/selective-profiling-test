package org.jetbrains.test.profiling;

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
     * @param callTree FullCallRecords to save into file.
     * @param filePath Relative to project dir(like "src/...")
     *                or absolute path("/usr/...")
     * @throws IOException WHen there are problems with output file.
     */
    public static void printSerialized(FullCallRecords callTree, String filePath) throws IOException {
        try (FileOutputStream outputStream = new FileOutputStream(FileUtil.getFileByPath(filePath))){
            ObjectOutputStream stream = new ObjectOutputStream(outputStream);
            stream.writeObject(callTree);
            stream.flush();
            outputStream.close();
        }
    }
}
