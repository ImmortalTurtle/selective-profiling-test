package org.jetbrains.test.profiling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Class is used to save information about
 * methods' names, arguments and call hierarchy.
 * It contains list of methods' points of entrance,
 * each of which is the root of {@link CallData} tree.
 * <b>For each {@link Thread} there is always no more
 * than one ThreadCallTree.</b>
 * List of active ThreadCallTrees is stored in
 * {@link FullCallRecords}.
 *
 * @author Egor Nemchinov
 */
public class ThreadCallTree implements Serializable, Comparable<ThreadCallTree>{
    private int index;
    transient private CallData deepestCall;
    private int maxLevel;

    /**
     * List of points of entrances.
     * Whole list is required because
     * profiling is selective, which means there are many methods
     * to track and sometimes they aren't connected one to another.
     */
    private List<CallData> roots;

    /**
     * Constructor.
     * Calls {@link ThreadCallTree#ThreadCallTree(int)} with
     * {@link Indexer#nextIndex()}
     */
    public ThreadCallTree() {
        this(Indexer.nextIndex());
    }

    /**
     * Initializes list with roots.
     * @param index Index of ThreadCallTree.
     */
    ThreadCallTree(int index) {
        this.index = index;
        this.roots = new ArrayList<>();
    }

    /**
     * Must be called when entering method.
     * @param callData CallData containing method's name
     *                 and arguments passed into it.
     */
    public void enter(CallData callData) {
        if(deepestCall == null) {
            roots.add(callData);
        } else {
            deepestCall.addChild(callData);
            callData.setParent(deepestCall);
        }
        deepestCall = callData;
    }

    /**
     * Must be called when exiting method.
     */
    public void exit() {
        if(deepestCall.getLevel() > maxLevel)
            maxLevel = deepestCall.getLevel();
        deepestCall = deepestCall.getParent();
    }

    /**
     * Prints all gathered call data.
     */
    void print() {
        for(CallData root: roots) {
            root.print();
            System.out.println();
        }
    }

    /**
     * Compares ThreadCallTrees by index.
     * Used to sort them.
     * @param threadCallTree ThreadCallTree
     * @return index.compareTo(other.index)
     */
    @Override
    public int compareTo(ThreadCallTree threadCallTree) {
        return Integer.compare(this.index, threadCallTree.getIndex());
    }

    /**
     * Index indicating order of initializing.
     * @return {@link ThreadCallTree#index}
     */
    public int getIndex() {
        return index;
    }

    /**
     * Returns list of {@link CallData} roots.
     * @return {@link ThreadCallTree#roots}
     */
    public List<CallData> getRoots() {
        return roots;
    }

    /**
     * Returns maximum depth among all
     * the trees.
     * @return Maximum level of {@link CallData}
     */
    public int getMaxLevel() {
        return maxLevel;
    }

    /**
     * Used for indexing ThreadCallTrees by the order of
     * their initializing.
     */
    static class Indexer {
        private static AtomicInteger index = new AtomicInteger(0);

        /**
         * Gets and increments index.
         * @return Number of ThreadCallTree initialized.
         */
        static int nextIndex() {
            return index.getAndIncrement();
        }

        /**
         * Sets index to 0.
         */
        static void nullify() {
            index.set(0);
        }
    }
}
