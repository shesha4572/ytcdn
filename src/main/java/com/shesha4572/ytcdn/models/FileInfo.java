package com.shesha4572.ytcdn.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

@RedisHash(value = "FileCdn" , timeToLive = 3600)
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FileInfo implements Serializable {
    @Id
    private String fileId;
    private LocalDateTime uploadedOn;
    private String fileName;
    private long size;
    private ArrayList<Chunk> chunkList;
}
