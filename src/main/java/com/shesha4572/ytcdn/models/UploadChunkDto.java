package com.shesha4572.ytcdn.models;

import lombok.Data;

import java.util.List;

@Data
public class UploadChunkDto {
    String chunkId;
    List<String> podNames;
    int chunkIndex;
}
