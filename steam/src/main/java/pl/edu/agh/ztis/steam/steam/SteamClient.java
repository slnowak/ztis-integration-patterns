package pl.edu.agh.ztis.steam.steam;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.functions.Func1;

import java.util.stream.Collectors;

public class SteamClient {

    private final String apiKey;
    private final SteamAPI steam;
    private final SteamStoreAPI store;

    public SteamClient(String apiKey) {
        this.steam = new Retrofit.Builder()
                .baseUrl(SteamAPI.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createAsync())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SteamAPI.class);

        this.store = new Retrofit.Builder()
                .baseUrl(SteamStoreAPI.BASE_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.createAsync())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(SteamStoreAPI.class);

        this.apiKey = apiKey;
    }

    public Observable<OwnedGame> gamesOwnedBy(SteamUser user) {
        return steam.getOwnedGames(apiKey, user.getUserId())
                .map(responseObject -> responseObject.response.games)
                .flatMapIterable(games -> games)
                .flatMap(game -> gameDetails(game.gameId))
                .filter(filterOutUnrecognized());
    }

    private Observable<OwnedGame> gameDetails(Long gameId) {
        return store.getDetails(gameId)
                .map(responseAsMap -> responseAsMap.get(String.valueOf(gameId)))
                .map(gameDetailsResponse -> gameDetailsResponse.data)
                .map(gameData -> new OwnedGame(
                        gameData.gameId,
                        gameData.name,
                        gameData.description,
                        gameData.genres.stream().map(genre -> genre.description).collect(Collectors.toList()))
                )
                .onErrorResumeNext(Observable.just(OwnedGame.UNKNOWN));
    }

    private Func1<OwnedGame, Boolean> filterOutUnrecognized() {
        return game -> !game.equals(OwnedGame.UNKNOWN);
    }
}
