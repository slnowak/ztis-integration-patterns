package pl.edu.agh.ztis.steam.steam;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

import java.util.Map;

interface SteamStoreAPI {

    String BASE_URL = "http://store.steampowered.com";

    @GET("/api/appdetails")
    Observable<Map<String, GameDetailsResponse>> getDetails(@Query("appids") Long apiKey);
}
