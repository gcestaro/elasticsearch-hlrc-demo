package com.github.gcestaro.services;

import static com.github.gcestaro.factories.ProductFactory.babyProduct;
import static com.github.gcestaro.factories.ProductFactory.oldProduct;
import static com.github.gcestaro.factories.ProductFactory.teenProduct;
import static org.elasticsearch.index.query.QueryBuilders.fuzzyQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchPhrasePrefixQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.elasticsearch.index.query.QueryBuilders.regexpQuery;
import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;

import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import com.github.gcestaro.models.Product;
import com.github.gcestaro.repositories.ProductRepository;
import com.github.gcestaro.resources.filters.ProductFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ProductService {

	private ElasticsearchOperations elasticsearchOperations;

	private ProductRepository repository;

	public ProductService(ProductRepository repository, ElasticsearchOperations elasticsearchOperations) {
		this.repository = repository;
		this.elasticsearchOperations = elasticsearchOperations;
	}

	public Page<Product> findAll(Pageable pageable) {
		return repository.findAll(pageable);
	}

	public List<Product> fullTextSearch(String text) {
		log.info("Full-text searching by: {}", text);

		QueryStringQueryBuilder queryStringQuery = queryStringQuery(text);

		return findProducts(queryStringQuery);
	}

	public List<Product> phraseSearch(String description) {
		log.info("Phrase searching by: {}", description);

		QueryBuilder matchPhraseQuery = matchPhraseQuery("description.raw", description);

		return findProducts(matchPhraseQuery);
	}

	public List<Product> fuzzySearch(String description) {
		log.info("Fuzzy searching by: {}", description);

		QueryBuilder fuzzyQueryBuilder = fuzzyQuery("description.raw", description);

		return findProducts(fuzzyQueryBuilder);
	}

	public List<Product> searchAsYouType(String description) {
		log.info("Partial-match searching by: {}", description);

		QueryBuilder matchPhrasePrefixQuery = matchPhrasePrefixQuery("description.raw", description)
				.slop(5);

		return findProducts(matchPhrasePrefixQuery);
	}

	public List<Product> partialMatchWildcardSearch(String wildcard) {
		log.info("Partial-match searching by wildcard: {}", wildcard);

		QueryBuilder wildcardQuery = wildcardQuery("description.raw", wildcard);

		return findProducts(wildcardQuery);
	}

	public List<Product> partialMatchRegularExpressionSearch(String regexp) {
		log.info("Partial-match searching by regular expression: {}", regexp);

		QueryBuilder regexpQuery = regexpQuery("description.raw", regexp);

		return findProducts(regexpQuery);
	}

	public Page<Product> searchPagingAndSorting(ProductFilter filter, Pageable pageable) {
		log.info("Page search by: {}", filter);

		BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

		if (filter.filterByDescription()) {
			boolQuery.must(QueryBuilders.fuzzyQuery("description.raw", filter.getDescription()));
		}

		if (filter.filterByRange()) {
			boolQuery.must(QueryBuilders.fuzzyQuery("description.raw", filter.getDescription()));
		}

		NativeSearchQuery query = new NativeSearchQueryBuilder()
				.withQuery(boolQuery)
				.withPageable(pageable)
				.build();

		SearchHits<Product> products = elasticsearchOperations.search(query, Product.class);

		logResult(products);

		return new PageImpl<>(products.map(SearchHit::getContent).toList());
	}

	private List<Product> findProducts(QueryBuilder queryBuilder) {
		NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
				.withQuery(queryBuilder)
				.build();

		SearchHits<Product> products = elasticsearchOperations.search(searchQuery, Product.class);

		logResult(products);

		return products.map(SearchHit::getContent).toList();
	}

	private void logResult(SearchHits<Product> products) {
		log.info("{} products returned", products.getTotalHits());
	}

	public void load() {
		repository.bulkInsert();
	}

	public void loadSimpleTest() {
		repository.save(babyProduct());

		repository.saveAll(List.of(teenProduct(), oldProduct()));
	}

	public void deleteAll() {
		repository.deleteAll();
	}
}
