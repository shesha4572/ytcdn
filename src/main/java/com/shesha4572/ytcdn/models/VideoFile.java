package com.shesha4572.ytcdn.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Document(collection = "videos")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class VideoFile {
    @MongoId
    private String id;
    @Indexed
    private String ownerId;
    private String ownerDisplayId;
    private long fileSizeBytes;
    @Indexed
    private String title;
    @Indexed(unique = true)
    private String internalFileId;
    private LocalDateTime uploadedOn;
    private String desc;
    private String thumbnailLink;
    private long viewCounter;
    private long likeCounter;
    private String mpdName;
    private Boolean isPlayable;

}
