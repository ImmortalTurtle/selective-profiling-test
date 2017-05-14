package org.jetbrains.test.profiling;

import java.io.*;

/**
 * Purpose of this class is to read file
 * and restore {@link FullCallRecords} from it,
 * which can be done by deserializing or
 * parsing data.
 *
 * @author Egor Nemchinov
 */
public class Reader {

    /**
     * Restores FullCallRecords from file.
     * File must contain previously serialized tree.
     * @param filePath Relative to project dir(like "src/...")
     *                or absolute path("/usr/...")
     * @return FullCallRecords, deserialized from file.
     * @throws IOException When there are problems with input file.
     * @throws ClassNotFoundException Some of deserialized classes aren't on the classpath.
     */
    public static FullCallRecords readSerialized(String filePath) throws IOException, ClassNotFoundException {
        try(FileInputStream inputStream = new FileInputStream(FileUtil.getFileByPath(filePath))) {
            FullCallRecords fullCallRecords = (FullCallRecords) new ObjectInputStream(inputStream).readObject();
            inputStream.close();
            return fullCallRecords;
        }
    }
}
