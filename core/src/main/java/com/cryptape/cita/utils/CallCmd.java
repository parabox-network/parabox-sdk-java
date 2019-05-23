package com.cryptape.cita.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class CallCmd {
    private static class StreamGobbler implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public StreamGobbler(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(
                    new InputStreamReader(this.inputStream))
                    .lines()
                    .forEach(this.consumer);
        }
    }

    public static class ExecutedResult {
        public int exitCode;
        public String output;

        public ExecutedResult(int exitCode, String output) {
            this.exitCode = exitCode;
            this.output = output;
        }
    }

    public static ExecutedResult callCmd(String cmd) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder();
        pb.directory(new File("/"));
        pb.command(cmd.split("\\s+"));
        Process p = pb.start();

        final String[] result = {""};
        Consumer<String> consumer = output -> result[0] = output;
        StreamGobbler streamGobbler = new StreamGobbler(p.getInputStream(), consumer);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = p.waitFor();
        return new ExecutedResult(exitCode, result[0]);
    }
}
