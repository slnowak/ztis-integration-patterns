package pl.edu.agh.ztis.orchestrator.recommendations.rabbitmq;

import com.google.gson.Gson;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.ShutdownSignalException;
import io.reactivex.Emitter;
import lombok.RequiredArgsConstructor;
import pl.edu.agh.ztis.orchestrator.recommendations.Recommendation;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.stream.Collectors;

@RequiredArgsConstructor
class RecommendationMessageConsumer implements Consumer {
    private final Emitter<Recommendation> emitter;
    private final Gson decoder;

    @Override
    public void handleConsumeOk(String consumerTag) {
    }

    @Override
    public void handleCancelOk(String consumerTag) {
        emitter.onComplete();
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
        emitter.onComplete();
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException sig) {
        emitter.onError(sig);
    }

    @Override
    public void handleRecoverOk(String consumerTag) {
    }

    @Override
    public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
        final RecommendationMessage recommendationMessage = encodeMessage(properties, body);
        emitter.onNext(recommendationFrom(recommendationMessage));
    }

    private RecommendationMessage encodeMessage(AMQP.BasicProperties properties, byte[] body) throws UnsupportedEncodingException {
        return decoder.fromJson(
                new String(body, Charset.defaultCharset()),
                RecommendationMessage.class
        );
    }

    private Recommendation recommendationFrom(RecommendationMessage recommendationMessage) {
        return new Recommendation(
                recommendationMessage.title,
                recommendationMessage.recommendations.stream().map(r -> r.name).collect(Collectors.toList())
        );
    }
}
