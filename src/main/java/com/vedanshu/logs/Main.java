package com.vedanshu.logs;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
//        int cores = Runtime.getRuntime().availableProcessors();
//        System.out.println("Starting Log Analyzer on " + cores + " cores...");
//
//        ExecutorService executor = Executors.newFixedThreadPool(cores);
//
//        for (int i = 1; i <= 5; i++) {
//            int taskId = i;
//            executor.submit(() -> {
//                System.out.println("Processing Chunk #" + taskId + " on Thread: " + Thread.currentThread().getName());
//            });
//        }
//
//        executor.shutdown();
        com.vedanshu.logs.util.LogGenerator.generate();
    }
}
