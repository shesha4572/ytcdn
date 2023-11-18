package com.shesha4572.ytcdn.controllers;

import com.shesha4572.ytcdn.models.SearchResult;
import com.shesha4572.ytcdn.models.VideoFile;
import com.shesha4572.ytcdn.services.AuthenticationService;
import com.shesha4572.ytcdn.services.CdnVideoService;

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/video")
@RequiredArgsConstructor
public class VideoController {
    private final CdnVideoService cdnVideoService;

    @GetMapping(value = "/get/{videoId}" , produces = "video/mp4")
    public ResponseEntity<Resource> getVideo(@PathVariable String videoId , @RequestHeader("Range") String range) {
        return cdnVideoService.getVideoRange(videoId , range);
    }

    @GetMapping("/details/{videoId}")
    public ResponseEntity<SearchResult> getDetails(@PathVariable String videoId){
        VideoFile videoFile = cdnVideoService.getVideoDetails(videoId);
        if(videoFile == null){
            return ResponseEntity.notFound().build();
        }
        SearchResult searchResult = SearchResult.builder()
                .internalFileId(videoFile.getInternalFileId())
                .desc(videoFile.getDesc())
                .thumbnailLink(videoFile.getThumbnailLink())
                .title(videoFile.getTitle())
                .viewCounter(videoFile.getViewCounter())
                .uploadedOn(videoFile.getUploadedOn())
                .ownerDisplayName(videoFile.getOwnerDisplayId())
                .likeCounter(videoFile.getLikeCounter())
                .build();
        return ResponseEntity.ok().body(searchResult);
    }

    @GetMapping("/search/{searchString}")
    public ResponseEntity<List<SearchResult>> getSearchResults(@PathVariable String searchString){
        List<SearchResult> results = cdnVideoService.getSearchResults(searchString);
        return ResponseEntity.ok().body(results);
    }

    @PostMapping("/view/{internalFileId}")
    public ResponseEntity<?> incrementView(@PathVariable String internalFileId){
        if(cdnVideoService.incrementViewCount(internalFileId)){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().build();
    }

}
