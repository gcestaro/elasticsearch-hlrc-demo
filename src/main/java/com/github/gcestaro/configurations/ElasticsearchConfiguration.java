package com.github.gcestaro.configurations;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@Configuration
@EnableElasticsearchRepositories
class ElasticsearchConfiguration extends AbstractElasticsearchConfiguration {

	@Override
	@Bean(destroyMethod = "close")
	public RestHighLevelClient elasticsearchClient() {
		return RestClients
				.create(ClientConfiguration
						.builder()
						.connectedTo("localhost:9200")
						.withSocketTimeout(30L * 1000)
						.withConnectTimeout(3L * 1000)
						.build())
				.rest();
	}
}