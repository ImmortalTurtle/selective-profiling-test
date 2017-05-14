package org.jetbrains.test.profiling;

import java.io.File;

/**
 * Class provides some useful functions
 * for working with Files.
 *
 * @author Egor Nemchinov
 */
public class FileUtil {

    /**
     * Decides whether path is relative to project directory or
     * absolute and returns corresponding file.
     * @param filePath Relative to project dir(like "src/...")
     *                or absolute path("/usr/...")
     * @return File corresponding to given filePath
     */
    public static File getFileByPath(String filePath) {
        if(filePath.charAt(0) == '/')
            return new File(filePath);
        else
            return new File(System.getProperty("user.dir") + "/" +filePath);
    }
}
