package org.jetbrains.test.profiling;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * What does it do?
 *
 * @author Egor Nemchinov
 */
public class CallTreeIterator implements Iterator<CallData> {
    private CallData current, next = null;
    private List<CallData> roots;
    private int treeIndex = 0;
    private Map<CallData, Integer> indexMap;

    public CallTreeIterator(ThreadCallTree callTree) {
        this.roots = callTree.getRoots();
        indexMap = new HashMap<>();
        this.current = roots.get(0);
        this.next = this.current;
    }

    @Override
    public boolean hasNext() {
        return next != null;
    }

    @Override
    public CallData next() {
        current = next;
        Integer childrenIndex = indexMap.get(next);
        if(childrenIndex == null) {
            childrenIndex = 0;
        }
        while(childrenIndex >= current.getChildren().size()) {
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
        indexMap.put(current, childrenIndex + 1);
        next = current.getChildren().get(childrenIndex);
        return current;
    }
}
