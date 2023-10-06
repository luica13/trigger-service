package org.jetbrains.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.dto.BuildDto;
import org.jetbrains.strategy.BuildTriggerStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuildTriggerService {

    private final BuildTriggerStrategy commitStrategy;
    private final BuildTriggerStrategy scheduledStrategy;

    public BuildTriggerService(
            @Qualifier("commitBuildTriggerStrategy") BuildTriggerStrategy commitStrategy,
            @Qualifier("scheduledBuildTriggerStrategy") BuildTriggerStrategy scheduledStrategy

    ) {
        this.commitStrategy = commitStrategy;
        this.scheduledStrategy = scheduledStrategy;
    }

    public void handleBuildRequest(BuildDto buildDto) {

        if (commitStrategy.shouldTrigger(buildDto)) {
            commitStrategy.triggerBuild(buildDto);
        } else if (scheduledStrategy.shouldTrigger(buildDto)) {
            scheduledStrategy.triggerBuild(buildDto);
        } else {
            log.warn("No strategy decided to trigger a build for repository: {} on branch: {}", buildDto.getRepositoryUrl(), buildDto.getBranch());
            throw new IllegalArgumentException("No correct strategy choosen");
        }
    }
}
