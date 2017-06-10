package pl.edu.agh.ztis.orchestrator.recommendations;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@EqualsAndHashCode
public class Recommendation {

    public final String gameBought;
    public final List<String> recommendedGames;
}
