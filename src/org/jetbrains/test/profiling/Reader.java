package org.jetbrains.test.profiling;

import javenue.csv.Csv;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

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
     *
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

    /**
     * Restores FullCallRecords from file.
     * File must contain previously saved into .csv tree.
     *
     * @param filePath Relative to project dir(like "src/...")
     *                 or absolute path("/usr/...")
     * @return Generated FullCallTree
     * @throws IOException When problems with input file.
     * @throws Csv.FormatException When .csv file is invalid.
     */
    public static FullCallRecords readCsv(String filePath) throws IOException, Csv.FormatException{
        Csv.Reader reader = new Csv.Reader(new FileReader(FileUtil.getFileByPath(filePath)))
                .delimiter(',').preserveSpaces(false);
        int level = 0;
        List<ThreadCallTree> callTrees = new ArrayList<>();
        ThreadCallTree callTree = new ThreadCallTree();
        List<String> currentString = reader.readLine();
        while(currentString != null && !currentString.isEmpty()) {
            if(level >= currentString.size()) {
                for (int i = 0; i < level - 1; i++) {
                    callTree.exit();
                }
                level = 0;
            }
            String cell = currentString.get(level++);
            //Another root was met
            if(cell.length() < 2) {
                for (int i = 0; i < level - 1; i++) {
                    callTree.exit();
                }
                level = 0;
                cell = currentString.get(level++);
            }
            if(cell.contains("Thread-")) {
                int index = Integer.parseInt(cell.substring(7, 8));
                callTree = new ThreadCallTree(index);
                callTrees.add(callTree);
                level--;
            } else if(!cell.isEmpty()) {
                String[] currentSplit = cell.split("\\(");
                String methodName = currentSplit[0];
                String[] arguments = currentSplit[1].substring(0, currentSplit[1].length() - 1).split(", ");
                callTree.enter(new CallData(methodName, arguments));
            }
            currentString = reader.readLine();
        }
        return new FullCallRecords(callTrees);
    }
}