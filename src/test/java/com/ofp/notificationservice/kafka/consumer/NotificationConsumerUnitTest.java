package com.ofp.notificationservice.kafka.consumer;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationConsumerUnitTest {

    private NotificationConsumer consumer;
    private ListAppender<ILoggingEvent> logAppender;
    private Logger logger;

    @BeforeEach
    void setUp() {
        consumer = new NotificationConsumer();
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
    void onPaymentAuthorized_shouldLogConfirmationMessage() {
        UUID orderId = UUID.randomUUID();
        String payload = "{\"orderId\":\"" + orderId + "\"}";

        consumer.onPaymentAuthorized(payload);

        List<ILoggingEvent> logs = logAppender.list;
        assertThat(logs).hasSize(2);
        assertThat(logs.get(0).getFormattedMessage())
                .contains("Payment authorized")
                .contains("order confirmation email");
        assertThat(logs.get(1).getFormattedMessage()).contains(payload);
    }

    @Test
    void onPaymentFailed_shouldLogFailureMessage() {
        UUID orderId = UUID.randomUUID();
        String payload = "{\"orderId\":\"" + orderId + "\"}";

        consumer.onPaymentFailed(payload);

        List<ILoggingEvent> logs = logAppender.list;
        assertThat(logs).hasSize(2);
        assertThat(logs.get(0).getFormattedMessage())
                .contains("Payment failed")
                .contains("payment failure email");
        assertThat(logs.get(1).getFormattedMessage()).contains(payload);
    }

    @Test
    void onStockRejected_shouldLogOutOfStockMessage() {
        UUID orderId = UUID.randomUUID();
        UUID customerId = UUID.randomUUID();
        String payload = """
                {"orderId":"%s","customerId":"%s","totalAmount":"49.50"}\
                """.formatted(orderId, customerId);

        consumer.onStockRejected(payload);

        List<ILoggingEvent> logs = logAppender.list;
        assertThat(logs).hasSize(2);
        assertThat(logs.get(0).getFormattedMessage())
                .contains("Stock rejected")
                .contains("out of stock email");
        assertThat(logs.get(1).getFormattedMessage()).contains(payload);
    }

    @Test
    void shouldHandleEmptyPayloadWithoutThrowing() {
        consumer.onPaymentAuthorized("");
        consumer.onPaymentFailed("");
        consumer.onStockRejected("");

        assertThat(logAppender.list).hasSize(6);
    }
}