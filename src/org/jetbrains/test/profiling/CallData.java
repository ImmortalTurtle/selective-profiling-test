package org.jetbrains.test.profiling;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Egor Nemchinov on 11.05.17.
 * SPbU, 2017
 */
public class CallData implements Serializable{
    private String methodName;
    private String[] arguments;
    private int level = 0;

    private CallData parent;
    private List<CallData> children = new ArrayList<>();

    public CallData(String methodName, String... arguments) {
        this.methodName = methodName;
        this.arguments = arguments;
    }

    void addChild(CallData callData) {
        children.add(callData);
    }

    void setParent(CallData parent) {
        this.parent = parent;
        level = parent.level + 1;
    }

    CallData getParent() {
        return parent;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < level; i++) {
            stringBuilder.append("-");
        }
        stringBuilder.append(methodName).append("(");
        stringBuilder.append(joinToString(arguments)).append(")");
        return stringBuilder.toString();
    }

    private String joinToString(String[] array) {
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < array.length; i++) {
            stringBuilder.append(array[i]);
            if(i < array.length - 1)
                stringBuilder.append(", ");
        }
        return stringBuilder.toString();
    }

    void print() {
        System.out.println(this.toString());
        for(CallData childCall: children) {
            childCall.print();
        }
    }
}
