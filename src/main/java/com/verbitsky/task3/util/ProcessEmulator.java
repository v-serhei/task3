package com.verbitsky.task3.util;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class ProcessEmulator {
    private static final int MIN_PROCESSING_TIME = 1;
    private static final int MAX_PROCESSING_TIME = 3;
    private static Random generator = new Random();

    private ProcessEmulator() {
    }

    public static void emulateProcessingOperation () {
        int delay = MIN_PROCESSING_TIME + generator.nextInt(MAX_PROCESSING_TIME - MIN_PROCESSING_TIME + 1);
        try {
            TimeUnit.SECONDS.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}