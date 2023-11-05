package com.shesha4572.ytcdn.controllers;

import com.shesha4572.ytcdn.services.CdnVideoService;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
public class VideoController {
    private final CdnVideoService cdnVideoService;

    @GetMapping(value = "/get/{videoId}" , produces = "video/mp4")
    public ResponseEntity<Resource> getVideo(@PathVariable String videoId , @RequestHeader("Range") String range) {
        return cdnVideoService.getVideoRange(videoId , range);
    }



}
