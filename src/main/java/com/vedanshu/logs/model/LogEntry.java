package com.vedanshu.logs.model;

public record LogEntry(
        String timestamp,
        String level,     // INFO, WARN, ERROR
        String endpoint,
        int statusCode,
        long responseTimeMs
) {}
