package com.github.gcestaro.repositories;

import static java.util.UUID.randomUUID;
import static org.elasticsearch.client.RequestOptions.DEFAULT;
import static org.elasticsearch.client.Requests.INDEX_CONTENT_TYPE;
import static org.elasticsearch.client.Requests.indexRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.elasticsearch.action.admin.indices.refresh.RefreshRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.gcestaro.models.Product;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
public class ProductCustomRepositoryImpl implements ProductCustomRepository {

	private static final int DATA_SET_BULK_INSERT_SIZE = 10_000;

	private static final int DATA_SET_SIZE = 1_000_000;

	private static final String FAKE_DESCRIPTION = "Product";

	private static final String PRODUCTS_INDEX = "products-elasticsearch-demo-index";

	private RestHighLevelClient restClient;

	private ObjectMapper objectMapper;

	public ProductCustomRepositoryImpl(RestHighLevelClient restClient, ObjectMapper objectMapper) {
		this.restClient = restClient;
		this.objectMapper = objectMapper;
	}

	public void bulkInsert() {
		log.info("--- Loading data into elasticsearch ---");

		List<Product> products = Stream
				.generate(Product::builder)
				.map(builder -> builder.id(randomUUID().toString())
						.description(FAKE_DESCRIPTION)
						.createdAt(LocalDateTime.now())
						.build())
				.limit(DATA_SET_SIZE)
				.collect(Collectors.toList());

		log.info("--- Loading {} products ---", products.size());

		Lists.partition(products, DATA_SET_BULK_INSERT_SIZE)
				.stream()
				.forEach(this::processProducts);
		
		log.info("--- products loaded ---");
	}

	private void processProducts(List<Product> productsList) {
		BulkRequest bulkRequest = new BulkRequest();

		productsList.stream()
				.map(productAsIndexRequest())
				.forEach(bulkRequest::add);

		sendRequest(bulkRequest);

		try {
			restClient.indices().refresh(new RefreshRequest(PRODUCTS_INDEX),
					RequestOptions.DEFAULT);
		} catch (IOException e) {
			log.error("Error while refreshing ES data");
		}
	}

	@SuppressWarnings("unchecked")
	private Function<Product, IndexRequest> productAsIndexRequest() {
		return product -> indexRequest(PRODUCTS_INDEX)
				.id(product.getId())
				.source(objectMapper.convertValue(product, Map.class), INDEX_CONTENT_TYPE);
	}

	private void sendRequest(BulkRequest bulkRequest) {
		try {
			BulkResponse bulkResponse = restClient.bulk(bulkRequest, DEFAULT);

			log.info("--- Data loaded into elasticsearch. Description: {}. Response {} ---",
					bulkRequest.getDescription(), bulkResponse.toString());
		} catch (IOException e) {
			log.error("--- Failed to load data into elasticsearch ---", e);
			throw new IllegalStateException("--- Failed to load data into Elasticsearch ---", e);
		}
	}
}
