package org.jetbrains.test;

import org.jetbrains.test.profiling.FullCallRecords;
import org.jetbrains.test.profiling.Printer;
import org.jetbrains.test.profiling.Reader;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        final int THREAD_AMOUNT = 3;
        final int TASKS_AMOUNT = 5;
        ExecutorService service = Executors.newFixedThreadPool(THREAD_AMOUNT);
        FullCallRecords.getInstance();
        for(int i = 0; i < TASKS_AMOUNT; i++) {
            int start = 100 * i;
            List<String> arguments = IntStream.range(start, start + 10)
                    .mapToObj(Integer :: toString)
                    .collect(Collectors.toList());
            service.submit(() -> {
                FullCallRecords.getInstance().registerThread();
                new DummyApplication(arguments).start();
            });
        }
        service.shutdown();
        try {
            service.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        FullCallRecords.getInstance().print();
        //Either absolute or relative to project directory path can be passed into these methods
//        Printer.printSerialized(FullCallRecords.getInstance(), "src/org/jetbrains/test/calls_serialized");
//        Reader.readSerialized("src/org/jetbrains/test/calls_serialized").print();
        Printer.printCsv(FullCallRecords.getInstance(), "src/org/jetbrains/test/calls.csv", ',');
        Reader.readCsv("src/org/jetbrains/test/calls.csv").print();
    }
}
