package org.jetbrains.test.profiling;

import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Egor Nemchinov on 11.05.17.
 * SPbU, 2017
 */
public class FullCallTree implements Serializable{
    private static FullCallTree instance;

    public FullCallTree() {
        ThreadCallTree.Indexer.nullify();
    }

    private List<ThreadCallTree> callTreeList = Collections.synchronizedList(new ArrayList<ThreadCallTree>(5));

    private transient ConcurrentHashMap<Thread, ThreadCallTree> threadMap = new ConcurrentHashMap<>(5);

    public ThreadCallTree currentCallTree() {
        ThreadCallTree callTree = threadMap.get(Thread.currentThread());
        if(callTree == null) {
            callTree = new ThreadCallTree();
            threadMap.put(Thread.currentThread(), callTree);
        }
        return callTree;
    }

    public void registerThread() {
        if(threadMap.get(Thread.currentThread()) == null) {
            ThreadCallTree callTree =  new ThreadCallTree();
            threadMap.put(Thread.currentThread(), callTree);
            callTreeList.add(callTree);
        }
    }

    public void print() {
        //sorting callTrees by order of running
        Collections.sort(callTreeList);
        for(ThreadCallTree tree: callTreeList) {
            System.out.println("Thread-"+ tree.index+ "'s call tree:");
            tree.print();
            System.out.println();
        }
    }

    public static FullCallTree getInstance() {
        if(instance == null)
            instance = new FullCallTree();
        return instance;
    }
}
