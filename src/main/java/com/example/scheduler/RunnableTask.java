package com.example.scheduler;

import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RunnableTask implements Runnable {

    private String message;

    public RunnableTask(String message) {
        this.message = message;
    }

    @Override
    public void run() {
        log.info(LocalDateTime.now() + " : Runnable Task with >" + message + "< on thread : " + Thread.currentThread()
                .getName());
    }
}
