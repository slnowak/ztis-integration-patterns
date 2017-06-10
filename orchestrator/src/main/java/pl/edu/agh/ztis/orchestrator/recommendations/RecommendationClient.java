package pl.edu.agh.ztis.orchestrator.recommendations;

import io.reactivex.Flowable;

public interface RecommendationClient {

    Flowable<Recommendation> recommendations();
}
