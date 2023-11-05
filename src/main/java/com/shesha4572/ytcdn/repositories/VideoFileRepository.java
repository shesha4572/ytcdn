package com.shesha4572.ytcdn.repositories;

import com.shesha4572.ytcdn.models.VideoFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VideoFileRepository extends MongoRepository<VideoFile , String> {
}
