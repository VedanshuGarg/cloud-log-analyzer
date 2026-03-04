package com.vedanshu.logs.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class LogGenerator {

    private static final String FILE_PATH = "server_logs.txt";
    private static final int TOTAL_LINES = 1_000_000;

    // Simulating realistic server traffic patterns
    private static final String[] LEVELS = {"INFO", "INFO", "INFO", "WARN", "ERROR"};
    private static final String[] ENDPOINTS = {"/api/users", "/api/transactions", "/api/auth", "/health"};
    private static final int[] STATUS_CODES = {200, 200, 201, 400, 401, 404, 500};

    public static void generate() {
        System.out.println("Generating " + TOTAL_LINES + " log lines. Please wait...");
        long startTime = System.currentTimeMillis();

        // Using BufferedWriter for high-speed disk writing
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            Random random = new Random();
            Instant now = Instant.now();

            for (int i = 0; i < TOTAL_LINES; i++) {
                String level = LEVELS[random.nextInt(LEVELS.length)];
                String endpoint = ENDPOINTS[random.nextInt(ENDPOINTS.length)];
                int statusCode = STATUS_CODES[random.nextInt(STATUS_CODES.length)];
                long responseTime = 10 + random.nextInt(2000);

                // Move time backwards slightly for each log to simulate a timeline
                String timestamp = now.minus(TOTAL_LINES - i, ChronoUnit.SECONDS).toString();

                // Format: 2026-03-03T10:15:30Z [ERROR] /api/transactions 500 1450ms
                String logLine = String.format("%s [%s] %s %d %dms\n", timestamp, level, endpoint, statusCode, responseTime);
                writer.write(logLine);
            }
        } catch (IOException e) {
            System.err.println("Error writing file: " + e.getMessage());
        }

        long endTime = System.currentTimeMillis();
        System.out.println("File generated successfully in " + (endTime - startTime) + "ms: " + FILE_PATH);
    }
}
