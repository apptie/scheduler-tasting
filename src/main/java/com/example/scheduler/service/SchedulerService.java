package com.example.scheduler.service;

import com.example.scheduler.RunnableTask;
import com.example.scheduler.dto.SchedulerRequest;
import com.example.scheduler.dto.SchedulerResponse;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SchedulerService {

    private static Long sequence = 0L;
    private static final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();

    private final ThreadPoolTaskScheduler threadPoolTaskScheduler;

    public SchedulerService(ThreadPoolTaskScheduler threadPoolTaskScheduler) {
        this.threadPoolTaskScheduler = threadPoolTaskScheduler;
    }

    public SchedulerResponse addScheduler(SchedulerRequest request) {
        final Instant now = Instant.now();
        final Instant target = now.plusSeconds(request.getSeconds());

        final ScheduledFuture<?> scheduledFuture = threadPoolTaskScheduler.schedule(
                new RunnableTask(request.getMessage()), target);

        scheduledTasks.put(++sequence, scheduledFuture);

        return new SchedulerResponse(sequence, now, target);
    }

    public void deleteScheduler(Long id) {
        final ScheduledFuture<?> targetScheduledFuture = scheduledTasks.get(id);

        if (targetScheduledFuture == null || targetScheduledFuture.isDone() || targetScheduledFuture.isCancelled()) {
            throw new RuntimeException("작동중인 스케줄러가 아닙니다.");
        }
        targetScheduledFuture.cancel(true);
        log.info("delete scheduler id : {}", id);
    }
}
