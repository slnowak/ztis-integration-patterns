package pl.edu.agh.ztis.steam.rabbitmq;

import com.google.gson.Gson;
import com.rabbitmq.client.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.Collections;
import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RabbitMQClient implements AutoCloseable {

    private static final Gson ENCODER = new Gson();

    private final Connection connection;
    private final Consumer<Object> messagePublisher;

    @SneakyThrows
    public static RabbitMQClient create(RabbitMQProperties props) {
        final Connection connection = setUpConnection(props);
        final Channel channel = boundChannel(connection, props);

        return new RabbitMQClient(
                connection,
                message -> publishMessageOn(channel, props, message)
        );
    }

    public static RabbitMQClient create(RabbitMQProperties.RabbitMQPropertiesBuilder props) {
        return create(props.build());
    }

    @SneakyThrows
    private static Connection setUpConnection(RabbitMQProperties props) {
        final ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername(props.getUsername());
        factory.setPassword(props.getPassword());
        factory.setHost(props.getHost());
        factory.setPort(props.getPort());

        return factory.newConnection();
    }

    @SneakyThrows
    private static Channel boundChannel(Connection connection, RabbitMQProperties props) {
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare(props.getExchangeName(), BuiltinExchangeType.TOPIC, false);
        channel.queueDeclare(props.getTopic(), false, false, false, Collections.emptyMap());
        channel.queueBind(
                channel.queueDeclare().getQueue(),
                props.getExchangeName(),
                props.getTopic()
        );

        return channel;
    }

    @SneakyThrows
    private static <T> void publishMessageOn(Channel channel, RabbitMQProperties props, T message) {
        channel.basicPublish(
                props.getExchangeName(),
                "*",
                MessageProperties.TEXT_PLAIN,
                ENCODER.toJson(message).getBytes()
        );
    }

    public <T> void publishMessage(T message) {
        messagePublisher.accept(message);
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}
