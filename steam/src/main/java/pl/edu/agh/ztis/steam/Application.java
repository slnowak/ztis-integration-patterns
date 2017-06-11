package pl.edu.agh.ztis.steam;

import lombok.SneakyThrows;
import pl.edu.agh.ztis.steam.rabbitmq.RabbitMQClient;
import pl.edu.agh.ztis.steam.rabbitmq.RabbitMQProperties;
import pl.edu.agh.ztis.steam.steam.SteamClient;
import pl.edu.agh.ztis.steam.steam.SteamUser;

import java.util.concurrent.TimeUnit;

public class Application {

    @SneakyThrows
    public static void main(String[] args) {
        System.out.println("Steam app started");

        final Environment environment = new Environment();

        final String steamApiKey = environment.getString("STEAM_API_KEY").orElseThrow(() -> new RuntimeException("steam api key missing"));
        final String steamUser = environment.getString("STEAM_USER").orElse("76561197960434622");

        final String rabbitHost = environment.getString("RABBIT_HOST").orElse("localhost");
        final Integer rabbitPort = environment.getInt("RABBIT_PORT").orElse(5672);
        final String rabbitUser = environment.getString("RABBIT_USER").orElse("guest");
        final String rabbitPassword = environment.getString("RABBIT_PASSWORD").orElse("guest");
        final String rabbitExchange = environment.getString("RABBIT_EXCHANGE").orElse("steam");
        final String rabbitTopic = environment.getString("RABBIT_TOPIC").orElse("game_bought");


        final SteamClient steamClient = new SteamClient(steamApiKey);
        final RabbitMQClient rabbitMQClient = RabbitMQClient.create(
                RabbitMQProperties.builder()
                        .host(rabbitHost)
                        .port(rabbitPort)
                        .username(rabbitUser)
                        .password(rabbitPassword)
                        .exchangeName(rabbitExchange)
                        .topic(rabbitTopic)
        );


        steamClient
                .gamesOwnedBy(SteamUser.ofId(steamUser))
                .delay(30, TimeUnit.SECONDS)
                .subscribe(game -> {
                    System.out.println("Game bought: " + game);
                    rabbitMQClient.publishMessage(game);
                });
    }
}
