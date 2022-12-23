package com.bookretail.util;

import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

public class LoggableFuture {
    public static void runAsync(Runnable runnable) {
        var logger = LoggerFactory.getLogger(runnable.getClass());

        CompletableFuture.runAsync(runnable).exceptionally(throwable -> {
            if (throwable instanceof CompletionException) {
                logger.error(throwable.getCause().getMessage(), throwable.getCause());
            } else {
                logger.error(throwable.getMessage(), throwable);
            }

            return null;
        });
    }
}
