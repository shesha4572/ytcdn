package com.shesha4572.ytcdn.services;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CdnVideoService {

    private final CdnFileService cdnFileService;
    public ResponseEntity<Resource> getVideoRange(String videoId, String range) {
        range = range.substring(6);
        String[] split = range.split("-");
        long startIndex = Long.parseLong(split[0]);
        long endIndex = (split.length > 1) ? Long.parseLong(split[1]) : -1;
        return cdnFileService.getVideoRange(videoId , startIndex , endIndex);
    }
}
