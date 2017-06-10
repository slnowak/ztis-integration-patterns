package pl.edu.agh.ztis.orchestrator.recommendations.rabbitmq;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import pl.edu.agh.ztis.orchestrator.recommendations.Recommendation;
import pl.edu.agh.ztis.orchestrator.recommendations.RecommendationClient;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RabbitMQRecommendationClient implements RecommendationClient {

    private final Channel channel;
    private final Gson DECODER = new Gson();

    public static RabbitMQRecommendationClient create(RabbitMQProperties.RabbitMQPropertiesBuilder props) {
        return create(props.build());
    }

    private static RabbitMQRecommendationClient create(RabbitMQProperties props) {
        return new RabbitMQRecommendationClient(ChannelFactory.from(props));
    }

    @Override
    public Flowable<Recommendation> recommendations() {
        return Flowable.create(
                emitter -> channel.basicConsume(
                        channel.queueDeclare().getQueue(),
                        true,
                        new RecommendationMessageConsumer(emitter, DECODER)
                ),
                BackpressureStrategy.BUFFER
        );
    }

}
