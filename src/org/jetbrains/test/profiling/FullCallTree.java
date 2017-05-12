package org.jetbrains.test.profiling;

import java.io.Serializable;
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

    private ConcurrentHashMap<Thread, ThreadCallTree> threadMap = new ConcurrentHashMap<>(5);

    public ThreadCallTree currentCallTree() {
        ThreadCallTree callTree = threadMap.get(Thread.currentThread());
        if(callTree == null) {
            callTree = new ThreadCallTree();
            threadMap.putIfAbsent(Thread.currentThread(), callTree);
        }
        return callTree;
    }

    public void registerThread() {
        threadMap.putIfAbsent(Thread.currentThread(), new ThreadCallTree());
    }

    public void print() {
        //sorting callTrees by order of running
        List<ThreadCallTree> callTrees = new ArrayList<>(threadMap.values());
        Collections.sort(callTrees);
        for(ThreadCallTree tree: threadMap.values()) {
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
