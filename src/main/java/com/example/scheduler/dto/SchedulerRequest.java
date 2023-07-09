package com.example.scheduler.dto;

public class SchedulerRequest {

    private Long seconds;
    private String message;

    public SchedulerRequest() {
    }

    public Long getSeconds() {
        return seconds;
    }

    public String getMessage() {
        return message;
    }
}
