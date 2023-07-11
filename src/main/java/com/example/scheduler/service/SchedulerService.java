package com.example.scheduler.service;

import com.example.scheduler.configuration.SchedulerComponentCreator;
import com.example.scheduler.entity.SchedulerJobInfo;
import com.example.scheduler.job.SimpleRepeatJob;
import com.example.scheduler.repository.SchedulerRepository;
import java.time.Instant;
import lombok.extern.slf4j.Slf4j;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerKey;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class SchedulerService {

    private static final long REPEAT_COUNT = 1L;

    private final SchedulerFactoryBean schedulerFactoryBean;
    private final SchedulerRepository schedulerRepository;
    private final SchedulerComponentCreator creator;
    private final ApplicationContext context;

    public SchedulerService(SchedulerFactoryBean schedulerFactoryBean,
            SchedulerRepository schedulerRepository, SchedulerComponentCreator creator,
            ApplicationContext context) {
        this.schedulerFactoryBean = schedulerFactoryBean;
        this.schedulerRepository = schedulerRepository;
        this.creator = creator;
        this.context = context;
    }

    public void createJob(SchedulerJobInfo jobInfo) {
        try {
            Scheduler scheduler = schedulerFactoryBean.getScheduler();

            JobDetail jobDetail = JobBuilder
                    .newJob(SimpleRepeatJob.class)
                    .withIdentity(jobInfo.getJobName(), jobInfo.getJobGroup())
                    .build();

            if (scheduler.checkExists(jobDetail.getKey())) {
                log.error("이미 등록한 Job 입니다.");
                return ;
            }

            jobDetail = creator.createJob(
                    SimpleRepeatJob.class,
                    false,
                    context,
                    jobInfo.getJobName(),
                    jobInfo.getJobGroup()
            );

            Trigger trigger = creator.createSimpleTrigger(
                    jobInfo.getJobName(),
                    Instant.now().plusSeconds(jobInfo.getSeconds()),
                    REPEAT_COUNT,
                    SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);

            scheduler.scheduleJob(jobDetail, trigger);
            jobInfo.setJobStatus("등록");

            schedulerRepository.save(jobInfo);

        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void updateJob(SchedulerJobInfo jobInfo) {
        Trigger newTrigger = creator.createSimpleTrigger(
                jobInfo.getJobName(),
                Instant.now().plusSeconds(jobInfo.getSeconds()),
                REPEAT_COUNT,
                SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW
        );

        try {
            final Scheduler scheduler = schedulerFactoryBean.getScheduler();
            scheduler.rescheduleJob(TriggerKey.triggerKey(jobInfo.getJobName()), newTrigger);
            jobInfo.setJobStatus("수정 후 재등록");
            schedulerRepository.save(jobInfo);
        } catch (SchedulerException e) {
            log.error(e.getMessage(), e);
        }
    }

    public boolean deleteJob(SchedulerJobInfo jobInfo) {
        try {
            SchedulerJobInfo getJobInfo = schedulerRepository.findByJobName(jobInfo.getJobName());
            schedulerRepository.delete(getJobInfo);
            return schedulerFactoryBean.getScheduler().deleteJob(new JobKey(jobInfo.getJobName(), jobInfo.getJobGroup()));
        } catch (SchedulerException e) {
            log.error("Job {} 삭제 실패", jobInfo.getJobName(), e);
            return false;
        }
    }
}
