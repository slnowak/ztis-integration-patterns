package pl.edu.agh.ztis.steam.steam;

import lombok.Value;

import java.util.Collections;
import java.util.List;

@Value
public class OwnedGame {

    public static final OwnedGame UNKNOWN = new OwnedGame(-1L, "unknown", "", Collections.emptyList());

    Long gameId;
    String title;
    String description;
    List<String> genres;
}
