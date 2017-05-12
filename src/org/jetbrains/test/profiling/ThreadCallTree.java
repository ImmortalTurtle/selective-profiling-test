package org.jetbrains.test.profiling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Egor Nemchinov on 11.05.17.
 * SPbU, 2017
 */
public class ThreadCallTree implements Serializable, Comparable<ThreadCallTree>{
    public int index = Indexer.nextIndex();
    transient private CallData deepestCall;
    private List<CallData> roots;

    public ThreadCallTree(CallData root) {
        this();
        enter(root);
    }

    public ThreadCallTree() {
        roots = new ArrayList<>();
    }

    public void enter(CallData callData) {
        if(deepestCall == null) {
            roots.add(callData);
        } else {
            deepestCall.addChild(callData);
            callData.setParent(deepestCall);
        }
        deepestCall = callData;
    }

    public void exit() {
        deepestCall = deepestCall.getParent();
    }

    void print() {
        for(CallData root: roots) {
            root.print();
        }
    }

    @Override
    public int compareTo(ThreadCallTree threadCallTree) {
        if(index < threadCallTree.index)
            return -1;
        else if(index == threadCallTree.index)
            return 0;
        else
            return 1;
    }

    static class Indexer {
        private static AtomicInteger index = new AtomicInteger();
        static int nextIndex() {
            return index.getAndIncrement();
        }

        static void nullify() {
            index.set(0);
        }
    }
}
