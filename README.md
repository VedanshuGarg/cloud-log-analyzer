# 📊 Cloud Log Analyzer

A high-performance, multi-threaded log parsing engine built with **Java 17**. Designed to ingest, process, and analyze massive server log files with minimal memory footprint and maximum CPU utilization.

![Java](https://img.shields.io/badge/Java-17-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Concurrency](https://img.shields.io/badge/Concurrency-ExecutorService-blue?style=for-the-badge)
![Performance](https://img.shields.io/badge/Performance-Optimized-success?style=for-the-badge)

## 🚀 Key Features

* **High-Speed Batch Processing:** Groups log entries into optimized chunks (10,000 lines/batch) before dispatching to worker threads, reducing context-switching overhead and cutting processing time by over 50%.
* **Memory Efficient (O(1) Heap):** Streams data directly from the disk using `BufferedReader`. It never loads the entire file into RAM, allowing it to parse multi-gigabyte files safely.
* **Thread-Safe Aggregation:** Utilizes non-blocking concurrent data structures (`AtomicInteger`, `ConcurrentHashMap`) to prevent race conditions without the performance penalty of `synchronized` blocks.
* **Dynamic Core Allocation:** Automatically scales the thread pool size based on the host machine's available CPU cores (`Runtime.getRuntime().availableProcessors()`).

## 📈 Performance Benchmarks

*Tested on a standard developer machine using dynamically generated dummy data.*

| Dataset Size | File Size | Execution Time (V1 - Single Line) | Execution Time (V2 - Batch) |
| :--- | :--- | :--- | :--- |
| **1,000,000 lines** | ~75 MB | 1614 ms | **837 ms** ⚡ |

## 🛠️ Architecture Overview

The application follows a highly optimized **Producer-Consumer** pattern:
1. **Producer (Main Thread):** Reads the `.txt` file line-by-line and fills a "bucket" (List) up to the target batch size.
2. **Task Queue:** The full bucket is submitted to the `ExecutorService`.
3. **Consumers (Worker Threads):** Threads grab a bucket, parse the strings into immutable Java 17 `LogEntry` records, and update the atomic counters.

## ⚡ Quick Start

**1. Clone the repository:**
  ```bash
  git clone [https://github.com/VedanshuGarg/cloud-log-analyzer.git](https://github.com/VedanshuGarg/cloud-log-analyzer.git)
  ```

**2. Generate the Test Data (1 Million Lines):**
  ```java
  com.vedanshu.logs.util.LogGenerator.generate();
  ```

**3. Run the Analyzer:**
  ```java
  com.vedanshu.logs.service.LogAnalyzer analyzer = new com.vedanshu.logs.service.LogAnalyzer();
  analyzer.analyze();
  ```

**Sample Output**
```bash
Starting parallel analysis (BATCH MODE) on 8 cores...

--- LOG ANALYSIS REPORT ---
Time taken: 837 ms
Total Requests Processed: 1000000
5xx Server Errors: 142857

Traffic by Endpoint:
   /api/users: 250102
   /api/transactions: 249890
   /health: 250005
   /api/auth: 250003
------------------------------
```

Built by Vedanshu Garg | Associate Software Engineer @CloudSufi
