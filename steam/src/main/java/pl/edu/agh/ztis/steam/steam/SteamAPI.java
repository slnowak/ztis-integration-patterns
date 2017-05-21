package pl.edu.agh.ztis.steam.steam;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

interface SteamAPI {

    String BASE_URL = "http://api.steampowered.com";

    @GET("/IPlayerService/GetOwnedGames/v001")
    Observable<OwnedGamesResponse> getOwnedGames(@Query("key") String apiKey, @Query("steamId") String userId);
}
