package pl.edu.agh.ztis.steam.rabbitmq;

import com.rabbitmq.client.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.util.function.Consumer;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RabbitMQClient implements AutoCloseable {

    private final Connection connection;
    private final Consumer<String> messagePublisher;

    @SneakyThrows
    public static RabbitMQClient create(RabbitMQProperties props) {
        final Connection connection = setUpConnection(props);
        final Channel channel = boundChannel(connection, props);

        return new RabbitMQClient(connection, message -> publishMessageOn(channel, props, message));
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
        channel.exchangeDeclare(props.getExchangeName(), BuiltinExchangeType.DIRECT, false);
        channel.queueBind(
                channel.queueDeclare().getQueue(),
                props.getExchangeName(),
                "*"
        );

        return channel;
    }

    @SneakyThrows
    private static void publishMessageOn(Channel channel, RabbitMQProperties props, String message) {
        channel.basicPublish(props.getExchangeName(), "*", MessageProperties.TEXT_PLAIN, message.getBytes());
    }

    public void publishMessage(String message) {
        messagePublisher.accept(message);
    }

    @Override
    public void close() throws Exception {
        connection.close();
    }
}