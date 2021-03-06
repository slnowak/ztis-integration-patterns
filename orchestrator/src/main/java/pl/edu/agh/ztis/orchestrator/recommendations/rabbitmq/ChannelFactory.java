package pl.edu.agh.ztis.orchestrator.recommendations.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

class ChannelFactory {

    static ChannelWithBoundQueue from(RabbitMQProperties props) {
        final Connection connection = setUpConnection(props);
        return boundChannel(connection, props);
    }

    @SneakyThrows
    private static Connection setUpConnection(RabbitMQProperties props) {
        final ConnectionFactory aFactory = new ConnectionFactory();
        aFactory.setUsername(props.getUsername());
        aFactory.setPassword(props.getPassword());
        aFactory.setHost(props.getHost());
        aFactory.setPort(props.getPort());

        return aFactory.newConnection();
    }

    @SneakyThrows
    private static ChannelWithBoundQueue boundChannel(Connection connection, RabbitMQProperties props) {
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare(props.getExchangeName(), BuiltinExchangeType.DIRECT);
        final String boundQueue = channel.queueDeclare().getQueue();
        channel.queueBind(
                boundQueue,
                props.getExchangeName(),
                props.getTopic()
        );

        return new ChannelWithBoundQueue(channel, boundQueue);
    }

    @RequiredArgsConstructor
    static class ChannelWithBoundQueue {
        final Channel channel;
        final String boundQueue;
    }
}
