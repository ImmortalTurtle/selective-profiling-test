package org.jetbrains.test;

import org.jetbrains.test.profiling.*;

import java.util.List;
import java.util.Random;

/**
 * Nikolay.Tropin
 * 18-Apr-17
 */
public class DummyApplication {
    private final List<String> args;
    private Random random = new Random(System.nanoTime());

    private ThreadCallTree threadCallTree;

    public DummyApplication(List<String> args) {
        this.args = args;
        threadCallTree = FullCallTree.getInstance().currentCallTree();
    }

    private boolean nextBoolean() {
        return random.nextBoolean();
    }

    private boolean stop() {
        return random.nextDouble() < 0.05;
    }

    private String nextArg() {
        int idx = random.nextInt(args.size());
        return args.get(idx);
    }

    private void sleep() {
        try {
            Thread.sleep(random.nextInt(20));
        } catch (InterruptedException ignored) {

        }
    }

    private void abc(String s) {
        threadCallTree.enter(new CallData("abc", s));

        sleep();
        if (stop()) {
            //do nothing
        }
        else if (nextBoolean()) {
            def(nextArg());
        }
        else {
            xyz(nextArg());
        }
        threadCallTree.exit();
    }

    private void def(String s) {
        threadCallTree.enter(new CallData("def", s));

        sleep();
        if (stop()) {
            //do nothing
        }
        else if (nextBoolean()) {
            abc(nextArg());
        }
        else {
            xyz(nextArg());
        }
        threadCallTree.exit();
    }

    private void xyz(String s) {
        threadCallTree.enter(new CallData("xyz", s));

        sleep();
        if (stop()) {
            //do nothing
        }
        else if (nextBoolean()) {
            abc(nextArg());
        }
        else {
            def(nextArg());
        }
        threadCallTree.exit();
    }

    public void start() {
        abc(nextArg());
    }
}
