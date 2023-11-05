package com.shesha4572.ytcdn.models;

import lombok.Data;

@Data

public class FileInitDto {
    String title;
    long fileSize;
    String desc;
    String thumbnailLink;
}
