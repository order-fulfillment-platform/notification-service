package com.ofp.notificationservice.kafka.consumer;

import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@SpringBootTest
@Testcontainers
class NotificaitonConsumerTest {

	@Container
	static KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.6.0"));


	@DynamicPropertySource
	static void overrideProperties(DynamicPropertyRegistry registry) {
		registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
	}


	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	@MockitoSpyBean
	private NotificationConsumer notificationConsumer;

    @Test
    void onPaymentAuthorized_shouldReceiveMessage() {
        kafkaTemplate.send("payment.authorized", "{\"orderId\":\"123\"}");

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() ->
            verify(notificationConsumer, atLeastOnce()).onPaymentAuthorized("{\"orderId\":\"123\"}")
        );
    }


    void onPaymentFailed_shouldReceiveMessage() {
        kafkaTemplate.send("payment.failed", "{\"orderId\":\"123\"}");

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() ->
            verify(notificationConsumer, atLeastOnce()).onPaymentFailed("{\"orderId\":\"123\"}")
        );
    }

    @Test
    void onStockRejected_shouldReceiveMessage() {
        kafkaTemplate.send("stock.rejected", "{\"orderId\":\"123\"}");

        await().atMost(10, TimeUnit.SECONDS).untilAsserted(() ->
            verify(notificationConsumer, atLeastOnce()).onStockRejected("{\"orderId\":\"123\"}")
        );
    }
}
