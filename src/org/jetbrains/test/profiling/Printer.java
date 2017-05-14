package org.jetbrains.test.profiling;

import java.io.*;

/**
 * Created by Egor Nemchinov on 14.05.17.
 * SPbU, 2017
 */
public class Printer {

    public static void printSerialized(FullCallTree callTree, String filePath) {
        try {
            FileOutputStream outputStream;
            if(filePath.charAt(0) == '/')
                outputStream = new FileOutputStream(new File(filePath));
            else
                outputStream = new FileOutputStream(new File(System.getProperty("user.dir") + "/" +filePath));
            ObjectOutputStream stream = new ObjectOutputStream(outputStream);
            stream.writeObject(callTree);
            stream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
