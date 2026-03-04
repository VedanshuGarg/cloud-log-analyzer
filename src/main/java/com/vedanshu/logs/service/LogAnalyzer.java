package com.vedanshu.logs.service;

import com.vedanshu.logs.model.LogEntry;
import com.vedanshu.logs.util.LogParser;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class LogAnalyzer {

    private static final String FILE_PATH = "server_logs.txt";

    private final AtomicInteger totalRequests = new AtomicInteger(0);
    private final AtomicInteger errorCount = new AtomicInteger(0);
    private final ConcurrentHashMap<String, AtomicInteger> endpointTraffic = new ConcurrentHashMap<>();

    public void analyze() {
        int cores = Runtime.getRuntime().availableProcessors();
        ExecutorService executor = Executors.newFixedThreadPool(cores);

        System.out.println("Starting parallel analysis on " + cores + " cores...");
        long startTime = System.currentTimeMillis();

        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            // 1. Read line-by-line
            while ((line = reader.readLine()) != null) {
                final String logLine = line;

                // 2. Hand the line to a background thread to parse and count
                executor.submit(() -> processLine(logLine));
            }
        } catch (IOException e) {
            System.err.println("Failed to read file: " + e.getMessage());
        }

        // 3. Tell threads to finish up, and wait for them
        executor.shutdown();
        try {
            executor.awaitTermination(1, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            System.err.println("Analysis interrupted!");
        }

        long endTime = System.currentTimeMillis();
        printReport(endTime - startTime);
    }

    // This method is called by multiple threads simultaneously
    private void processLine(String line) {
        LogEntry entry = LogParser.parse(line);
        if (entry == null) return;

        totalRequests.incrementAndGet();

        if (entry.statusCode() >= 500) {
            errorCount.incrementAndGet();
        }

        endpointTraffic.computeIfAbsent(entry.endpoint(), k -> new AtomicInteger(0)).incrementAndGet();
    }

    private void printReport(long durationMs) {
        System.out.println("\n --- LOG ANALYSIS REPORT ---");
        System.out.println("Time taken: " + durationMs + " ms");
        System.out.println("Total Requests Processed: " + totalRequests.get());
        System.out.println("5xx Server Errors: " + errorCount.get());
        System.out.println("\nTraffic by Endpoint:");
        endpointTraffic.forEach((endpoint, count) ->
                System.out.println("   " + endpoint + ": " + count.get())
        );
        System.out.println("------------------------------");
    }
}