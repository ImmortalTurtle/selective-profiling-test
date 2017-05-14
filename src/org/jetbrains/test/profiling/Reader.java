package org.jetbrains.test.profiling;

import java.io.*;

/**
 * Created by Egor Nemchinov on 14.05.17.
 * SPbU, 2017
 */
public class Reader {

    public static FullCallTree readSerialized(String filePath) {
        FileInputStream inputStream;
        FullCallTree fullCallTree = null;
        try {
            if(filePath.charAt(0) == '/')
                inputStream = new FileInputStream(new File(filePath));
            else
                inputStream = new FileInputStream(new File(System.getProperty("user.dir") + "/" +filePath));
            fullCallTree = (FullCallTree) new ObjectInputStream(inputStream).readObject();
            inputStream.close();
            return fullCallTree;
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return fullCallTree;
    }
}
