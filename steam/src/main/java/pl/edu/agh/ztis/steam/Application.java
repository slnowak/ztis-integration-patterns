package pl.edu.agh.ztis.steam;

import lombok.SneakyThrows;
import pl.edu.agh.ztis.steam.rabbitmq.RabbitMQClient;
import pl.edu.agh.ztis.steam.rabbitmq.RabbitMQProperties;

import java.util.concurrent.TimeUnit;

public class Application {

    @SneakyThrows
    public static void main(String[] args) {
        System.out.println("Steam app started");

        final RabbitMQClient client = RabbitMQClient.create(
                RabbitMQProperties.builder().host("rabbitmq").build()
        );

        client.publishMessage("foo");
        client.publishMessage("bar");
        client.publishMessage("baz");


        while (true) {
            TimeUnit.SECONDS.sleep(15);
        }
    }
}
