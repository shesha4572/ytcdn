package com.shesha4572.ytcdn.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.ArrayList;

@Document(collection = "likeMappings")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeMappings {
    @MongoId
    String id;
    @Indexed(unique = true)
    String username;
    ArrayList<String> likedVideoInternalFileIds;

}
