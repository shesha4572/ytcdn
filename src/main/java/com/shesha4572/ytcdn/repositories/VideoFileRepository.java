package com.shesha4572.ytcdn.repositories;

import com.shesha4572.ytcdn.models.VideoFile;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VideoFileRepository extends MongoRepository<VideoFile , String> {
    List<VideoFile> findVideoFilesByTitleContainsIgnoreCase(String searchString);

    List<VideoFile> findVideoFilesByOwnerId(String ownerId);

    Optional<VideoFile> findVideoFileByInternalFileId(String internalFileID);
}
