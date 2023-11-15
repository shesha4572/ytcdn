package com.shesha4572.ytcdn.repositories;

import com.shesha4572.ytcdn.models.FileInfo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileInfoRedisRepository extends CrudRepository<FileInfo , String> {
}
