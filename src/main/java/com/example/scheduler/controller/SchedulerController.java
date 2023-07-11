package com.example.scheduler.controller;

import com.example.scheduler.entity.SchedulerJobInfo;
import com.example.scheduler.service.SchedulerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/schedule")
public class SchedulerController {

    private final SchedulerService schedulerService;

    public SchedulerController(SchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    @PostMapping
    public ResponseEntity<Void> createJob(@RequestBody SchedulerJobInfo jobInfo) {
        schedulerService.createJob(jobInfo);

        return ResponseEntity.noContent()
                .build();
    }

    @PutMapping
    public ResponseEntity<Void> updateJob(@RequestBody SchedulerJobInfo jobInfo) {
        schedulerService.updateJob(jobInfo);

        return ResponseEntity.noContent()
                .build();
    }

    @DeleteMapping()
    public ResponseEntity<Void> deleteJob(@RequestBody SchedulerJobInfo jobInfo) {
        schedulerService.deleteJob(jobInfo);

        return ResponseEntity.noContent()
                .build();
    }
}
