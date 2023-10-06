package org.jetbrains.queue;

import org.jetbrains.dto.BuildDto;
import org.jetbrains.github.GitHubService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.util.SerializationUtils;

import static org.mockito.Mockito.verify;

public class BuildRequestConsumerTest {

    @Mock
    private GitHubService gitHubService;

    @InjectMocks
    private BuildRequestConsumer buildRequestConsumer;

    @Test
    public void testHandleBuildRequest() {
        MockitoAnnotations.openMocks(this);

        // Given
        BuildDto buildDto = new BuildDto();
        buildDto.setRepositoryUrl("https://github.com/ownerName/repoName.git");
        buildDto.setBranch("main");
        byte[] payload = SerializationUtils.serialize(buildDto);

        // When
        buildRequestConsumer.handleBuildRequest(payload);

        // Then
        verify(gitHubService).addWebhook(
                "ownerName",
                "repoName",
                "https://d911-37-252-83-175.ngrok.io/webhook"
        );
    }
}
