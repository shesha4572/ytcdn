package com.shesha4572.ytcdn.repositories;

import com.shesha4572.ytcdn.models.LikeMappings;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeMappingRepository extends MongoRepository<LikeMappings , String> {
    Optional<LikeMappings> findLikeMappingsByUsername(String username);
}
