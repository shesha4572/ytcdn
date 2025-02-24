package com.shesha4572.ytcdn.services;

import com.shesha4572.ytcdn.models.LikeMappings;
import com.shesha4572.ytcdn.models.VideoFile;
import com.shesha4572.ytcdn.repositories.LikeMappingRepository;
import com.shesha4572.ytcdn.repositories.VideoFileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {

    private final LikeMappingRepository likeMappingRepository;
    private final VideoFileRepository videoFileRepository;
    public boolean likeVideo(String internalFileId, String username) {
        Optional<VideoFile> optionalVideoFile = videoFileRepository.findVideoFileByInternalFileId(internalFileId);
        if(optionalVideoFile.isEmpty()){
            return false;
        }
        VideoFile videoFile = optionalVideoFile.get();
        Optional<LikeMappings> optionalLikeMappings = likeMappingRepository.findLikeMappingsByUsername(username);
        LikeMappings likeMappings = optionalLikeMappings.orElse(
                LikeMappings.builder()
                        .username(username)
                        .likedVideoInternalFileIds(new ArrayList<>())
                        .build()
        );
        if(likeMappings.getLikedVideoInternalFileIds().contains(videoFile.getInternalFileId())){
            return true;
        }
        ArrayList<String> updatedList = likeMappings.getLikedVideoInternalFileIds();
        updatedList.add(videoFile.getInternalFileId());
        likeMappings.setLikedVideoInternalFileIds(updatedList);
        videoFile.setLikeCounter(videoFile.getLikeCounter() + 1);
        videoFileRepository.save(videoFile);
        likeMappingRepository.save(likeMappings);
        return true;
    }

    public boolean hasLikedVideo(String internalFileId, String username) {
        Optional<LikeMappings> optionalLikeMappings = likeMappingRepository.findLikeMappingsByUsername(username);
        return optionalLikeMappings.map(likeMappings -> likeMappings.getLikedVideoInternalFileIds().contains(internalFileId)).orElse(false);
    }
}
