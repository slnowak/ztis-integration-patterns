package pl.edu.agh.ztis.orchestrator.recommendations;

import rx.Observable;

public interface RecommendationClient {

    Observable<Recommendation> recommendations();
}
