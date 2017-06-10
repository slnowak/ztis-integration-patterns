package pl.edu.agh.ztis.orchestrator.recommendations.rabbitmq;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class RabbitMQProperties {

    @Builder.Default
    String username = "guest";
    @Builder.Default
    String password = "guest";
    @Builder.Default
    String host = "localhost";
    @Builder.Default
    int port = 5672;

    @Builder.Default
    String exchangeName = "sample_exchange";
    @Builder.Default
    String topic = "sample_topic";
}
