package com.shesha4572.ytcdn.services;


import com.shesha4572.ytcdn.models.SearchResult;
import com.shesha4572.ytcdn.models.VideoFile;
import com.shesha4572.ytcdn.repositories.VideoFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CdnVideoService {

    private final CdnFileService cdnFileService;
    private final VideoFileRepository videoFileRepository;
    public ResponseEntity<Resource> getVideoRange(String videoId, String range) {
        range = range.substring(6);
        String[] split = range.split("-");
        long startIndex = Long.parseLong(split[0]);
        long endIndex = (split.length > 1) ? Long.parseLong(split[1]) : -1;
        return cdnFileService.getVideoRange(videoId , startIndex , endIndex);
    }

    public VideoFile getVideoDetails(String videoId) {
        Optional<VideoFile> file = videoFileRepository.findVideoFileByInternalFileId(videoId);
        return file.orElse(null);
    }

    public List<SearchResult> getSearchResults(String searchString) {
        List<VideoFile> videoFiles = videoFileRepository.findVideoFilesByTitleContainsIgnoreCase(searchString);
        List<SearchResult> results = new ArrayList<>();
        videoFiles.forEach(
                videoFile -> results.add(
                        SearchResult.builder()
                                .internalFileId(videoFile.getInternalFileId())
                                .desc(videoFile.getDesc())
                                .thumbnailLink(videoFile.getThumbnailLink())
                                .title(videoFile.getTitle())
                                .viewCounter(videoFile.getViewCounter())
                                .uploadedOn(videoFile.getUploadedOn())
                                .ownerDisplayName(videoFile.getOwnerDisplayId())
                                .likeCounter(videoFile.getLikeCounter())
                                .build()
                )
        );
        return results;
    }

    public Boolean incrementViewCount(String internalFileId) {
        Optional<VideoFile> optionalVideoFile = videoFileRepository.findVideoFileByInternalFileId(internalFileId);
        if(optionalVideoFile.isEmpty()){
            return false;
        }
        VideoFile file = optionalVideoFile.get();
        file.setViewCounter(file.getViewCounter() + 1);
        videoFileRepository.save(file);
        return true;
    }

    public Boolean setVideoPlayable(String internalFileId , String mpdName){
        Optional<VideoFile> optionalVideoFile = videoFileRepository.findVideoFileByInternalFileId(internalFileId);
        if(optionalVideoFile.isEmpty()){
            return false;
        }
        VideoFile file = optionalVideoFile.get();
        file.setMpdName(mpdName);
        file.setIsPlayable(Boolean.TRUE);
        videoFileRepository.save(file);
        return true;
    }
}
