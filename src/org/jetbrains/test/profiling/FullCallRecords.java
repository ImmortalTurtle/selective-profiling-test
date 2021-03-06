package org.jetbrains.test.profiling;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class consists of map for {@link Thread}s and
 * corresponding {@link ThreadCallTree}s, which is used
 * during the process of profiling, and list of
 *{@link ThreadCallTree}s, which is used in times of
 * printing call trees and performing operations
 * with gathered data, also {@link FullCallRecords#callTreeList}
 * is used for writing and reading FullCallTree into file.
 *
 * @author Egor Nemchinov
 */
public class FullCallRecords implements Serializable{
    //Singleton fits well because we should be able to profile easily
    //without carrying around an instance of FullCallRecords
    private static FullCallRecords instance;

    /**
     * List, containing ThreadCallTrees, is filled in the process
     * of deserialization and in the process of profiling.
     * After profiling is finished callTreeList is used to
     * view, print, and implement any operations with
     * gathered data.
     */
    private List<ThreadCallTree> callTreeList = Collections.synchronizedList(new ArrayList<ThreadCallTree>(5));

    /**
     * Used only in the process of profiling
     * to get ThreadCallTree for current Thread.
     */
    private transient ConcurrentHashMap<Thread, ThreadCallTree> threadMap = new ConcurrentHashMap<>(5);

    /**
     * Constructor.
     * Sets {@link ThreadCallTree.Indexer#index} to 0.
     */
    public FullCallRecords() {
        ThreadCallTree.Indexer.nullify();
    }

    public FullCallRecords(List<ThreadCallTree> callTreeList) {
        this();
        this.callTreeList = callTreeList;
    }

    /**
     * Called in the process of profiling for task to get
     * corresponding ThreadCallTree depending on Thread.currentThread().
     * @return Current Thread's ThreadCallTree.
     */
    public ThreadCallTree currentCallTree() {
        ThreadCallTree callTree = threadMap.get(Thread.currentThread());
        if(callTree == null) {
            callTree = new ThreadCallTree();
            threadMap.put(Thread.currentThread(), callTree);
        }
        return callTree;
    }

    /**
     * Adds new ThreadCallTree for current Thread to the map
     * and to the list. Must be called before the task
     * is executed, not right before the CallData is to be added.
     * Otherwise a problem can occur: currentCallTree() will be called
     * while map is being rebuilt after adding ThreadCallTree for current thread.
     */
    public void registerThread() {
        //It is indeed a bit unefficient to add it to both list and map
        //Can be solved by copying threadMap.values() into list after
        //the profiling is done.
        if(threadMap.get(Thread.currentThread()) == null) {
            ThreadCallTree callTree =  new ThreadCallTree();
            threadMap.put(Thread.currentThread(), callTree);
            callTreeList.add(callTree);
        }
    }

    /**
     * Calculates maximum depth among it's {@link ThreadCallTree}s,
     * i.e. maximum of all {@link ThreadCallTree#getMaxLevel()}
     * @return The biggest {@link ThreadCallTree#getMaxLevel()} among all ThreadCallTrees
     */
    public int calculateMaxLevel() {
        int maxLevel = 0;
        for(ThreadCallTree callTree: callTreeList) {
            if(callTree.getMaxLevel() > maxLevel)
                maxLevel = callTree.getMaxLevel();
        }
        return maxLevel;
    }

    /**
     * Prints {@link FullCallRecords#callTreeList} to console in a readable format.
     */
    public void print() {
        //Sorting ThreadCallTrees by order of creation.
        Collections.sort(callTreeList);
        for(ThreadCallTree tree: callTreeList) {
            System.out.println("Thread-"+ tree.getIndex()+ "'s call tree:");
            tree.print();
            System.out.println();
        }
    }

    /**
     * Gets {@link ThreadCallTree} list.
     * @return {@link FullCallRecords#callTreeList}
     */
    public List<ThreadCallTree> getCallTreeList() {
        return callTreeList;
    }

    /**
     * Singleton property.
     * @return {@link FullCallRecords#instance}
     */
    public static FullCallRecords getInstance() {
        if(instance == null)
            instance = new FullCallRecords();
        return instance;
    }
}
