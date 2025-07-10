package fr.epsi.service.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.SubscriberFactory;
import com.google.pubsub.v1.PubsubMessage;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderCreatedSubscriber {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public OrderCreatedSubscriber(
            SubscriberFactory subscriberFactory
    ) {
        Subscriber subscriber = subscriberFactory.createSubscriber(
                "order-created-sub",
                (PubsubMessage message, AckReplyConsumer consumer) -> {
                    String data = message.getData().toStringUtf8();

                    try {
                        List<Integer> orderIds = objectMapper.readValue(data, new TypeReference<>() {});
                        System.out.println("✅ Reçu : " + orderIds);

                        for (Integer orderId : orderIds) {
                            System.out.println("➡️ Traitement de la commande : " + orderId);
                        }

                        consumer.ack();

                    } catch (Exception e) {
                        System.err.println("❌ Erreur JSON : " + e.getMessage());
                        consumer.nack();
                    }
                }
        );

        subscriber.startAsync().awaitRunning();
        System.out.println("🔄 Abonné au topic Pub/Sub");
    }
}
