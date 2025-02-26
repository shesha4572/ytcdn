package com.shesha4572.ytcdn.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2/video")
@RequiredArgsConstructor
public class DashVideoController {
//    public ResponseEntity<Resource> getVideoChunks(@PathVariable String resourceId) {
//        return cdnVideoService.getVideoRange(resourceId);
//    }
}
