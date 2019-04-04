/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.integration.kafka.config.xml;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler;
import org.springframework.integration.support.DefaultErrorMessageStrategy;
import org.springframework.integration.test.util.TestUtils;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

/**
 * @author Gary Russell
 * @since 3.2
 *
 */
@SpringJUnitConfig
@DirtiesContext
public class KafkaOutboundGatewayParserTests {

	@Autowired
	@Qualifier("allProps.handler")
	KafkaProducerMessageHandler<?, ?> messageHandler;

	@Autowired
	private ApplicationContext context;

	@Test
	public void testProps() {
		assertThat(TestUtils.getPropertyValue(this.messageHandler, "errorMessageStrategy")).isInstanceOf(EMS.class);
		assertThat(TestUtils.getPropertyValue(this.messageHandler, "kafkaTemplate"))
			.isSameAs(this.context.getBean("template"));
		assertThat(this.messageHandler.getOrder()).isEqualTo(23);
		assertThat(TestUtils.getPropertyValue(this.messageHandler, "topicExpression.expression")).isEqualTo("'topic'");
		assertThat(TestUtils.getPropertyValue(this.messageHandler, "messageKeyExpression.expression"))
			.isEqualTo("'key'");
		assertThat(TestUtils.getPropertyValue(this.messageHandler, "partitionIdExpression.expression")).isEqualTo("2");
		assertThat(TestUtils.getPropertyValue(this.messageHandler, "sync", Boolean.class)).isTrue();
		assertThat(TestUtils.getPropertyValue(this.messageHandler, "sendTimeoutExpression.expression")).isEqualTo("44");
		assertThat(TestUtils.getPropertyValue(this.messageHandler, "timestampExpression.expression"))
				.isEqualTo("T(System).currentTimeMillis()");

		assertThat(TestUtils.getPropertyValue(this.messageHandler, "errorMessageStrategy"))
				.isSameAs(this.context.getBean("ems"));
		assertThat(TestUtils.getPropertyValue(this.messageHandler, "sendFailureChannel"))
				.isSameAs(this.context.getBean("failures"));
		assertThat(TestUtils.getPropertyValue(this.messageHandler, "sendSuccessChannel"))
				.isSameAs(this.context.getBean("successes"));
	}

	public static class EMS extends DefaultErrorMessageStrategy {

	}

}