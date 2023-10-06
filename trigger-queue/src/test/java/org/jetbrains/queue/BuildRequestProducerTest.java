package org.jetbrains.queue;

import org.jetbrains.dto.BuildDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.util.SerializationUtils;

import static org.mockito.Mockito.verify;

public class BuildRequestProducerTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private BuildRequestProducer producer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSendBuildRequest() {
        // Given
        String repoUrl = "https://github.com/sample/repo.git";
        String branch = "main";
        BuildDto request = new BuildDto(repoUrl, branch, false);

        // When
        producer.sendBuildDto(request);

        // Then
        verify(rabbitTemplate).send(new Message(SerializationUtils.serialize(request)));
    }
}
