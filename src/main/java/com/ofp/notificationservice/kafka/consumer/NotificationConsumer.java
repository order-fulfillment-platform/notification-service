package com.ofp.notificationservice.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class NotificationConsumer {

	@KafkaListener(topics = "payment.authorized", groupId = "notification-service")
	public void onPaymentAuthorized(String message) {
		log.info("[NOTIFICATION] Payment authorized- sending order confirmation email");
		log.info("Payload: {}", message);
	}

	@KafkaListener(topics = "payment.failed", groupId = "notification-service")
	public void onPaymentFailed(String message) {
		log.info("[NOTIFICATION] Payment failed - sending payment failure email");
		log.info("Payload: {}", message);
	}

	@KafkaListener(topics = "stock.rejected", groupId = "notification-service")
	public void onStockRejected(String message) {
		log.info("[NOTIFICATION] Stock rejected - sending out of stock email");
		log.info("Payload: {}", message);
	}
}
