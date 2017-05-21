package pl.edu.agh.ztis.steam;

import lombok.SneakyThrows;
import pl.edu.agh.ztis.steam.steam.OwnedGame;
import pl.edu.agh.ztis.steam.steam.SteamClient;
import pl.edu.agh.ztis.steam.steam.SteamUser;
import rx.Observable;

public class Application {

    @SneakyThrows
    public static void main(String[] args) {
        System.out.println("Steam app started");

        final SteamClient client = new SteamClient(
                "changeme"
        );

        final Observable<OwnedGame> games = client.gamesOwnedBy(SteamUser.ofId("76561197960434622"));
        games.subscribe(
                System.out::println,
                Throwable::printStackTrace
        );


    }
}
