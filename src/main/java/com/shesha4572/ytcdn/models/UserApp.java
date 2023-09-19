package com.shesha4572.ytcdn.models;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Document
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @MongoId
    private String id;

    private String name;

    private 
}
