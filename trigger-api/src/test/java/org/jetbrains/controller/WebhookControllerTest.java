package org.jetbrains.controller;

import org.jetbrains.dto.BuildDto;
import org.jetbrains.strategy.CommitBuildTriggerStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

public class WebhookControllerTest {
    @Mock
    private CommitBuildTriggerStrategy commitBuildTriggerStrategy;

    @InjectMocks
    private WebhookController webhookController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleGitHubWebhookCorrectPayload() {
        Map<String, Object> payload = new HashMap<>();
        Map<String, Object> repoMap =  new HashMap<>();
        repoMap.put("url", "https://github.com/test/repo.git");
        repoMap.put("ref", "refs/heads/master");
        repoMap.put("repository", "asd");
        payload.put("repository", repoMap);
        payload.put("ref", "refs/heads/master");

        ResponseEntity<Void> response = webhookController.handleGitHubWebhook(payload);

        assertEquals(200, response.getStatusCodeValue());
        verify(commitBuildTriggerStrategy).triggerBuild(new BuildDto("https://github.com/test/repo.git", "master", false));
    }

    @Test
    public void testHandleGitHubWebhookIncorrectPayload() {
        Map<String, Object> payload = new HashMap<>();

        assertThrows(IllegalArgumentException.class, () -> webhookController.handleGitHubWebhook(payload));

        payload.put("repository", new HashMap<>());
        assertThrows(IllegalArgumentException.class, () -> webhookController.handleGitHubWebhook(payload));
    }
}
