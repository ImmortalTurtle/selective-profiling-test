package org.jetbrains.test.profiling;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Iterator for {@link ThreadCallTree}.
 * Goes through all the {@link CallData}
 * in the tree in the order of their
 * invocation time.
 *
 * @author Egor Nemchinov
 */
public class CallTreeIterator implements Iterator<CallData> {
    private CallData current, next = null;
    private List<CallData> roots;
    private int treeIndex = 0;
    private Map<CallData, Integer> indexMap;

    /**
     * Constructor.
     * Creates iterator with starting point
     * at the first root of given {@link ThreadCallTree}
     * @param callTree {@link ThreadCallTree} to iterate in
     */
    public CallTreeIterator(ThreadCallTree callTree) {
        this.roots = callTree.getRoots();
        indexMap = new HashMap<>();
        this.current = roots.get(0);
        this.next = this.current;
    }

    /**
     * Determines whether there is next {@link CallData} node or not.
     * @return Boolean - Is there next {@link CallData}?
     */
    @Override
    public boolean hasNext() {
        return next != null;
    }

    /**
     * Method finds next CallData, which means
     * with the lowest time of invocation after current.
     * @return Next {@link CallData}
     */
    @Override
    public CallData next() {
        current = next;
        Integer childrenIndex = indexMap.get(next);
        if(childrenIndex == null) {
            childrenIndex = 0;
        }
        while(childrenIndex >= next.getChildren().size()) {
            next = next.getParent();
            if(next == null) {
                treeIndex++;
                if(treeIndex < roots.size()) {
                    next = roots.get(treeIndex);
                } else {
                    next = null;
                }
                return current;
            }
            childrenIndex = indexMap.get(next);
        }
        indexMap.put(next, childrenIndex + 1);
        next = next.getChildren().get(childrenIndex);
        return current;
    }
}
