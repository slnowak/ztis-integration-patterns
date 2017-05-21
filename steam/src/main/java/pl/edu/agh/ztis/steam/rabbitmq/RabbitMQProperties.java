package pl.edu.agh.ztis.steam.rabbitmq;

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
    String host = "rabbitmq";
    @Builder.Default
    int port = 5672;

    @Builder.Default
    String exchangeName = "steam_events";
    @Builder.Default
    String topic = "steam";
}
