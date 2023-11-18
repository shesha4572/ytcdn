package com.shesha4572.ytcdn.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SearchResult{
    private String title;
    private String internalFileId;
    private LocalDateTime uploadedOn;
    private String desc;
    private String thumbnailLink;
    private long viewCounter;
    private String ownerDisplayName;
    private long likeCounter;
}
