package com.ofp.notificationservice;

import org.springframework.boot.test.context.TestConfiguration;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.kafka.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

	@Container
	static final KafkaContainer kafka = new KafkaContainer(
	    DockerImageName.parse("apache/kafka:3.7.0")
	);

}
