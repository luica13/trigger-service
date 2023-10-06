package org.jetbrains.controller;

import org.jetbrains.dto.BuildDto;
import org.jetbrains.strategy.CommitBuildTriggerStrategy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/webhook")
public class WebhookController {

    private final CommitBuildTriggerStrategy commitBuildTriggerStrategy;

    public WebhookController(CommitBuildTriggerStrategy commitBuildTriggerStrategy) {
        this.commitBuildTriggerStrategy = commitBuildTriggerStrategy;
    }

    @PostMapping("/github")
    public ResponseEntity<Void> handleGitHubWebhook(@RequestBody Map<String, Object> payload) {

        if (!payload.containsKey("repository")) {
            throw new IllegalArgumentException("Invalid payload provided");
        }

        Map<String, Object> repositoryMap = (Map<String, Object>) payload.get("repository");

        if (!repositoryMap.containsKey("url") || !repositoryMap.containsKey("ref")) {
            throw new IllegalArgumentException("Invalid repository provided");
        }

        String repoUrl = repositoryMap.get("url").toString();
        String branch = payload.get("ref").toString().replace("refs/heads/", "");

        BuildDto buildRequestDto = new BuildDto(repoUrl, branch, false);
        commitBuildTriggerStrategy.triggerBuild(buildRequestDto);

        return ResponseEntity.ok().build();
    }
}
