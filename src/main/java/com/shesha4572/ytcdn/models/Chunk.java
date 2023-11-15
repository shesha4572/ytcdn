package com.shesha4572.ytcdn.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@RedisHash(value = "ChunksCdn" , timeToLive = 3600)
@Builder
@NoArgsConstructor
public class Chunk implements Serializable {
    @Id
    private String chunkId;
    @Indexed
    private String fileId;
    private int chunkIndex;
    private ArrayList<String> replicaPodList;
}
