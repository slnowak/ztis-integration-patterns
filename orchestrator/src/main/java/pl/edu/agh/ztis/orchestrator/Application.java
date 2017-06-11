package pl.edu.agh.ztis.orchestrator;

import pl.edu.agh.ztis.orchestrator.recommendations.RecommendationClient;
import pl.edu.agh.ztis.orchestrator.recommendations.rabbitmq.RabbitMQProperties;
import pl.edu.agh.ztis.orchestrator.recommendations.rabbitmq.RabbitMQRecommendationClient;
import pl.edu.agh.ztis.orchestrator.websockets.JettyWebSocketServerFactory;
import pl.edu.agh.ztis.orchestrator.websockets.WebSocketProperties;
import pl.edu.agh.ztis.orchestrator.websockets.WebSocketsServer;

public class Application {

    public static void main(String[] args) {
        System.out.println("Orchestrator app started");

        final Environment environment = new Environment();

        final String rabbitHost = environment.getString("RABBIT_HOST").orElse("localhost");
        final Integer rabbitPort = environment.getInt("RABBIT_PORT").orElse(5672);
        final String rabbitUser = environment.getString("RABBIT_USER").orElse("guest");
        final String rabbitPassword = environment.getString("RABBIT_PASSWORD").orElse("guest");
        final String rabbitExchange = environment.getString("RABBIT_EXCHANGE").orElse("recommendations");
        final String rabbitTopic = environment.getString("RABBIT_TOPIC").orElse("recommendations_prepared");

        final RecommendationClient recommendationClient = RabbitMQRecommendationClient.create(
                RabbitMQProperties.builder()
                        .host(rabbitHost)
                        .port(rabbitPort)
                        .username(rabbitUser)
                        .password(rabbitPassword)
                        .exchangeName(rabbitExchange)
                        .topic(rabbitTopic)
        );

        final WebSocketsServer webSockets = JettyWebSocketServerFactory.create(WebSocketProperties.builder());

        recommendationClient
                .recommendations()
                .subscribe(
                        recommendation -> {
                            System.out.println("About to send recommendation: " + recommendation);
                            webSockets.publish(recommendation);
                        },
                        error -> System.out.println("Got error: " + error),
                        () -> System.out.println("End of recommendation stream")
                );
    }
}
