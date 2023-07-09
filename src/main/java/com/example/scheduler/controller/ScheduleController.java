package com.example.scheduler.controller;

import com.example.scheduler.dto.SchedulerRequest;
import com.example.scheduler.dto.SchedulerResponse;
import com.example.scheduler.service.SchedulerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final SchedulerService schedulerService;

    public ScheduleController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @PostMapping
    public ResponseEntity<SchedulerResponse> create(@RequestBody SchedulerRequest request) {
        return ResponseEntity.ok(schedulerService.addScheduler(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        schedulerService.deleteScheduler(id);

        return ResponseEntity.noContent().build();
    }
}
