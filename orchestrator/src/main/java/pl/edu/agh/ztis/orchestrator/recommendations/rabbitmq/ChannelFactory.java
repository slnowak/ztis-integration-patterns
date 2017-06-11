package pl.edu.agh.ztis.orchestrator.recommendations.rabbitmq;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import lombok.SneakyThrows;

import java.util.Collections;

class ChannelFactory {

    static Channel from(RabbitMQProperties props) {
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
    private static Channel boundChannel(Connection connection, RabbitMQProperties props) {
        final Channel channel = connection.createChannel();
        channel.exchangeDeclare(props.getExchangeName(), BuiltinExchangeType.DIRECT);
        channel.queueBind(
                channel.queueDeclare().getQueue(),
                props.getExchangeName(),
                props.getTopic()
        );

        return channel;
    }
}
