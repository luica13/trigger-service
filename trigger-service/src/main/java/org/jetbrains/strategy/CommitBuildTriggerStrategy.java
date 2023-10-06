package org.jetbrains.strategy;

import jakarta.transaction.Transactional;
import lombok.Setter;
import org.jetbrains.dto.BuildDto;
import org.jetbrains.entity.JobHistory;
import org.jetbrains.queue.BuildRequestProducer;
import org.jetbrains.repository.JobHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDateTime;

@Component
public class CommitBuildTriggerStrategy implements BuildTriggerStrategy {

    private final JobHistoryRepository jobHistoryRepository;

    private final BuildRequestProducer buildRequestProducer;

    @Setter
    @Autowired
    private Clock clock = Clock.systemDefaultZone();

    public CommitBuildTriggerStrategy(JobHistoryRepository jobHistoryRepository, BuildRequestProducer buildRequestProducer) {
        this.jobHistoryRepository = jobHistoryRepository;
        this.buildRequestProducer = buildRequestProducer;
    }

    @Override
    public boolean shouldTrigger(BuildDto request) {
        return !request.isScheduled();
    }

    @Override
    @Transactional
    public void triggerBuild(BuildDto buildDto) {
        buildRequestProducer.sendBuildDto(buildDto);

        // Save the job start in history
        JobHistory history = new JobHistory();
        history.setRepositoryUrl(buildDto.getRepositoryUrl());
        history.setBranch(buildDto.getBranch());
        history.setStartTime(LocalDateTime.now(clock));
        history.setTriggeredBy("COMMIT");
        jobHistoryRepository.save(history);
    }
}
