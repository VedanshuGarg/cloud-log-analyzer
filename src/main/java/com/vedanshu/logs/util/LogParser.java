package com.vedanshu.logs.util;

import com.vedanshu.logs.model.LogEntry;

public class LogParser {

    // Example Log Line we are parsing:
    // "2026-03-03T10:15:30Z [ERROR] /api/transactions 500 1450ms"

    public static LogEntry parse(String rawLine) {
        try {
            String[] parts = rawLine.split(" ");

            if (parts.length < 5) {
                return null;
            }

            String timestamp = parts[0];
            String level = parts[1].replace("[", "").replace("]", ""); // Remove brackets
            String endpoint = parts[2];
            int statusCode = Integer.parseInt(parts[3]);

            // Remove the "ms" at the end of the response time and parse to Long
            long responseTime = Long.parseLong(parts[4].replace("ms", ""));

            return new LogEntry(timestamp, level, endpoint, statusCode, responseTime);

        } catch (Exception e) {
            System.err.println("Failed to parse line: " + rawLine);
            return null;
        }
    }
}
