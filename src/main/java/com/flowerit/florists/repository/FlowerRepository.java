package com.flowerit.florists.repository;

import com.flowerit.florists.domain.Flower;
import com.flowerit.florists.service.dto.FlowerDTO;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Spring Data MongoDB repository for the Flower entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FlowerRepository extends MongoRepository<Flower, String> {
    Page<Flower> findByCreatedBy(String userId, Pageable pageable);
}
