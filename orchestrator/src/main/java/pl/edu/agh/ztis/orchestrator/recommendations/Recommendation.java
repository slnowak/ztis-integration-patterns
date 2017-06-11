package pl.edu.agh.ztis.orchestrator.recommendations;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.List;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Recommendation {

    public final String gameBought;
    public final List<String> recommendedGames;
}
