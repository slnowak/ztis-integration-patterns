package pl.edu.agh.ztis.orchestrator.recommendations.rabbitmq;

import java.util.List;

class RecommendationMessage {

    String title;
    List<Recommendation> recommendations;

    static class Recommendation {
        String name;
    }
}
