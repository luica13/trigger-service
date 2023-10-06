package org.jetbrains.queue;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.dto.BuildDto;
import org.jetbrains.github.GitHubService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.utils.SerializationUtils;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BuildRequestConsumer {

    private final GitHubService gitHubService;

    public BuildRequestConsumer(GitHubService gitHubService) {
        this.gitHubService = gitHubService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.template.default-receive-queue}")
    public void handleBuildRequest(byte[] payload) {
        BuildDto buildDto = (BuildDto) SerializationUtils.deserialize(payload);

        log.info("building branch:{} of repo:{} ", buildDto.getBranch(), buildDto.getRepositoryUrl());

        String[] parts = buildDto.getRepositoryUrl().split("/");
        int length = parts.length;

        String repoNameWithGit = parts[length - 1];
        String repoName = repoNameWithGit.replace(".git", "");
        String ownerName = parts[length - 2];

        // here should be our endpoint , can be configured from app.properties
        gitHubService.addWebhook(ownerName, repoName, "https://d911-37-252-83-175.ngrok.io/webhook");

    }
}
