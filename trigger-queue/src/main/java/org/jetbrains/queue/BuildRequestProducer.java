package org.jetbrains.queue;

import org.jetbrains.dto.BuildDto;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.SerializationUtils;

@Service
public class BuildRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    public BuildRequestProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendBuildDto(BuildDto buildDto) {
        rabbitTemplate.send(new Message(SerializationUtils.serialize(buildDto)));
    }
}
