package org.jetbrains.test.profiling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Class stores method's name and arguments.
 * It is in fact a root of a tree consisting of
 * CallData for methods, which were called from
 * the method corresponding to this CallData during
 * it's working time. {@link ThreadCallTree} keeps
 * track of all the roots for these call trees.
 *
 * @author Egor Nemchinov
 */
public class CallData implements Serializable{
    private String methodName;
    private String[] arguments;
    private int level = 0;

    private CallData parent;
    private List<CallData> children = new ArrayList<>();

    /**
     * Constructor.
     * Takes name of the method and arguments passed into it
     * @param methodName Name of the tracked method.
     * @param arguments Arguments passed into the method.
     */
    public CallData(String methodName, String... arguments) {
        this.methodName = methodName;
        this.arguments = arguments;
    }

    /**
     * Adds CallData to this CallData's children.
     * @param callData Child to add.
     */
    void addChild(CallData callData) {
        children.add(callData);
    }

    /**
     * Parent CallData, may be null.
     * @return This CallData's parent.
     */
    CallData getParent() {
        return parent;
    }

    /**
     * Sets {@link CallData#getParent()}
     * @param parent Parent CallData value.
     */
    void setParent(CallData parent) {
        this.parent = parent;
        level = parent.level + 1;
    }

    /**
     * Transofrms CallData to string like:
     * "methodName(arg1, arg2, ...)"
     * @return String tranformed from CallData.
     */
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < level; i++) {
            stringBuilder.append("-");
        }
        stringBuilder.append(methodName).append("(");
        stringBuilder.append(joinToString(arguments, ", ")).append(")");
        return stringBuilder.toString();
    }

    /**
     * Joins array of strings into one string,
     * separating them using given string
     * @param array Array of strings
     * @param separatingString Separator
     * @return Joined string
     */
    private String joinToString(String[] array, String separatingString) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < array.length; i++) {
            stringBuilder.append(array[i]);
            if(i < array.length - 1)
                stringBuilder.append(separatingString);
        }
        return stringBuilder.toString();
    }

    /**
     * Prints this CallData and recursively all of it's children.
     */
    void print() {
        System.out.println(this.toString());
        for(CallData childCall: children) {
            childCall.print();
        }
    }
}
