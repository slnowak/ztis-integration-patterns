package pl.edu.agh.ztis.steam.steam;


import java.util.List;

class OwnedGamesResponse {

    Response response;

    static class Response {
        List<OwnedGameResponse> games;
    }
}
