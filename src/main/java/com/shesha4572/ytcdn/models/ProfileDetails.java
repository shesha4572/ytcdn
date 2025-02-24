package com.shesha4572.ytcdn.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProfileDetails {
    private String name;
    private String displayName;
    private LocalDateTime createdOn;
    private List<SearchResult> uploadedVideos;
}
