package pl.edu.agh.ztis.steam.steam;

import com.google.gson.annotations.SerializedName;

import java.util.List;

class GameDetailsResponse {

    Data data;

    static class Data {

        @SerializedName("steam_appid")
        Long gameId;

        String name;

        @SerializedName("short_description")
        String description;

        List<Genre> genres;
    }

    static class Genre {
        Long id;
        String description;
    }
}