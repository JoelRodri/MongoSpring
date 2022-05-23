package com.example.springmongo.repositories;

import com.example.springmongo.model.Product;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface ProductDao extends MongoRepository<Product, Integer> {
}
