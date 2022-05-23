package com.example.springmongo.repositories;

import com.example.springmongo.model.Shop;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShopDao extends MongoRepository<Shop, Integer> {

}
