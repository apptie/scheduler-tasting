package com.example.scheduler.dto;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class SchedulerResponse {

    private final Long id;
    private final LocalDateTime startTime;
    private final LocalDateTime targetTime;

    public SchedulerResponse(Long id, Instant startTime, Instant targetTime) {
        this.id = id;

        ZoneId zoneId = ZoneId.of("Asia/Seoul");
        this.startTime = LocalDateTime.ofInstant(startTime, zoneId);
        this.targetTime = LocalDateTime.ofInstant(targetTime, zoneId);
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getTargetTime() {
        return targetTime;
    }
}
