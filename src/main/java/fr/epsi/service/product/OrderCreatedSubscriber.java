package fr.epsi.service.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import com.google.cloud.spring.pubsub.support.SubscriberFactory;
import com.google.pubsub.v1.PubsubMessage;
import fr.epsi.service.product.dto.ProductCommandDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderCreatedSubscriber {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public OrderCreatedSubscriber(SubscriberFactory subscriberFactory, ProductService productService) {

        Subscriber subscriber = subscriberFactory.createSubscriber(
                "order-created-sub",
                (PubsubMessage message, AckReplyConsumer consumer) -> {
                    String data = message.getData().toStringUtf8();

                    try {
                        List<ProductCommandDto> productList = objectMapper.readValue(
                                data,
                                new TypeReference<>() {}
                        );

                        for (ProductCommandDto cmd : productList) {
                            productService.reduceProductQuantity(cmd.getIdProduit(), cmd.getQuantity());
                        }
                        consumer.ack();
                    } catch (Exception e) {
                        consumer.nack();
                    }
                }
        );
        subscriber.startAsync().awaitRunning();
    }
}
