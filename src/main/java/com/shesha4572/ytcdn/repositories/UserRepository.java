package com.shesha4572.ytcdn.repositories;


import com.shesha4572.ytcdn.models.UserApp;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<UserApp, String> {

    Optional<UserApp> findByUsername(String username);
    Boolean existsByUsernameEquals(String username);

    Optional<UserApp> findByDisplayName(String displayName);
}
