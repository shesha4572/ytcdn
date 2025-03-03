package com.shesha4572.ytcdn.controllers;

import com.shesha4572.ytcdn.services.DashResourceService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/video")
@RequiredArgsConstructor
public class DashVideoController {

    private final DashResourceService dashResourceService;

    @GetMapping("/get/{resourceId}")
    public ResponseEntity<Resource> getVideoChunks(@PathVariable String resourceId) {
        return dashResourceService.getWholeFile(resourceId);
    }
}
