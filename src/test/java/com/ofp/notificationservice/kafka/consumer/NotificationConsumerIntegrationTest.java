package com.ofp.notificationservice.kafka.consumer;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.TestPropertySource;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@EmbeddedKafka(
        partitions = 1,
        topics = { "payment.authorized", "payment.failed", "stock.rejected" },
        brokerProperties = { "listeners=PLAINTEXT://localhost:0", "port=0" }
)
@TestPropertySource(properties = {
        "spring.kafka.bootstrap-servers=${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.auto-offset-reset=earliest",
        "spring.kafka.consumer.group-id=notification-service"
})
class NotificationConsumerIntegrationTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private ListAppender<ILoggingEvent> logAppender;
    private Logger logger;

    @BeforeEach
    void setUp() {
        logger = (Logger) LoggerFactory.getLogger(NotificationConsumer.class);
        logAppender = new ListAppender<>();
        logAppender.start();
        logger.addAppender(logAppender);
    }

    @AfterEach
    void tearDown() {
        logger.detachAppender(logAppender);
    }

    @Test
    void shouldConsumePaymentAuthorizedEvent() {
        UUID orderId = UUID.randomUUID();
        String payload = "{\"orderId\":\"" + orderId + "\"}";

        kafkaTemplate.send("payment.authorized", orderId.toString(), payload);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() ->
                assertThat(logAppender.list)
                        .extracting(ILoggingEvent::getFormattedMessage)
                        .anyMatch(msg -> msg.contains("Payment authorized"))
                        .anyMatch(msg -> msg.contains(orderId.toString()))
        );
    }

    @Test
    void shouldConsumePaymentFailedEvent() {
        UUID orderId = UUID.randomUUID();
        String payload = "{\"orderId\":\"" + orderId + "\"}";

        kafkaTemplate.send("payment.failed", orderId.toString(), payload);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() ->
                assertThat(logAppender.list)
                        .extracting(ILoggingEvent::getFormattedMessage)
                        .anyMatch(msg -> msg.contains("Payment failed"))
                        .anyMatch(msg -> msg.contains(orderId.toString()))
        );
    }

    @Test
    void shouldConsumeStockRejectedEvent() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        String payload = """
                {"orderId":"%s","customerId":"%s","totalAmount":"49.50"}\
                """.formatted(orderId, customerId);

        kafkaTemplate.send("stock.rejected", orderId.toString(), payload);

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() ->
                assertThat(logAppender.list)
                        .extracting(ILoggingEvent::getFormattedMessage)
                        .anyMatch(msg -> msg.contains("Stock rejected"))
                        .anyMatch(msg -> msg.contains(orderId.toString()))
        );
    }
}