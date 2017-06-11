package pl.edu.agh.ztis.orchestrator.recommendations.rabbitmq;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import pl.edu.agh.ztis.orchestrator.recommendations.Recommendation;
import pl.edu.agh.ztis.orchestrator.recommendations.RecommendationClient;
import rx.Emitter;
import rx.Observable;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class RabbitMQRecommendationClient implements RecommendationClient {

    private final Channel channel;
    private final String queueName;
    private final Gson DECODER = new Gson();

    public static RabbitMQRecommendationClient create(RabbitMQProperties.RabbitMQPropertiesBuilder props) {
        return create(props.build());
    }

    private static RabbitMQRecommendationClient create(RabbitMQProperties props) {
        final ChannelFactory.ChannelWithBoundQueue channelWithQueue = ChannelFactory.from(props);
        return new RabbitMQRecommendationClient(channelWithQueue.channel, channelWithQueue.boundQueue);
    }

    @Override
    public Observable<Recommendation> recommendations() {
        return Observable.create(
                this::streamRecommendations,
                Emitter.BackpressureMode.BUFFER
        );
    }

    @SneakyThrows
    private String streamRecommendations(Emitter<Recommendation> emitter) {
        return channel.basicConsume(
                queueName,
                true,
                new RecommendationMessageConsumer(emitter, DECODER)
        );
    }
}
