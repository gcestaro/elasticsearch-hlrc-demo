package com.github.gcestaro.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.github.gcestaro.models.Product;

public interface ProductRepository extends ElasticsearchRepository<Product, String>, ProductCustomRepository {

}
