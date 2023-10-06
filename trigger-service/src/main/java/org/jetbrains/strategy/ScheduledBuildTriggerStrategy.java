package org.jetbrains.strategy;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.dto.BuildDto;
import org.jetbrains.entity.JobHistory;
import org.jetbrains.repository.JobHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
@Slf4j
public class ScheduledBuildTriggerStrategy implements BuildTriggerStrategy {

    private final JobHistoryRepository jobHistoryRepository;

    // A concurrent collection to keep track of scheduled build requests.
    private final List<BuildDto> scheduledBuildRequests = new CopyOnWriteArrayList<>();

    @Autowired
    public ScheduledBuildTriggerStrategy(JobHistoryRepository jobHistoryRepository) {
        this.jobHistoryRepository = jobHistoryRepository;
    }

    // This method checks if the scheduled job should be triggered.
    // Since we use @Scheduled for actual triggering, this just checks for any manual request for a scheduled build.
    @Override
    public boolean shouldTrigger(BuildDto requestDto) {
        return requestDto.isScheduled();
    }

    @Override
    public void triggerBuild(BuildDto requestDto) {
        scheduledBuildRequests.add(requestDto);
    }

    @Scheduled(cron = "${scheduled.build.cron}")
    @Transactional
    public void scheduledBuildTask() {
        for (BuildDto request : scheduledBuildRequests) {
            processBuildRequest(request);
        }
    }

    private void processBuildRequest(BuildDto buildDto) {
        log.info("Scheduled building of branch:{} of repo:{} ", buildDto.getBranch(), buildDto.getRepositoryUrl());

        JobHistory history = new JobHistory();
        history.setRepositoryUrl(buildDto.getRepositoryUrl());
        history.setBranch(buildDto.getBranch());
        history.setStartTime(LocalDateTime.now());
        history.setTriggeredBy("SCHEDULE");
        jobHistoryRepository.save(history);
    }

    public Collection<BuildDto> getScheduledBuildRequests() {
        return this.scheduledBuildRequests;
    }
}
