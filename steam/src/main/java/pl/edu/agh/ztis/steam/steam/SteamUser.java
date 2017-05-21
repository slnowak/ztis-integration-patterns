package pl.edu.agh.ztis.steam.steam;

import lombok.Value;

@Value(staticConstructor = "ofId")
public class SteamUser {

    String userId;
}
