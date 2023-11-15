package com.shesha4572.ytcdn.repositories;

import com.shesha4572.ytcdn.models.Chunk;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChunkRedisRepository extends CrudRepository<Chunk , String> {
}
