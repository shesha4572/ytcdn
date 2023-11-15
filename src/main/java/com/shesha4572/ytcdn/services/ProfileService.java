package com.shesha4572.ytcdn.services;

import com.shesha4572.ytcdn.models.ProfileDetails;
import com.shesha4572.ytcdn.models.SearchResult;
import com.shesha4572.ytcdn.models.UserApp;
import com.shesha4572.ytcdn.models.VideoFile;
import com.shesha4572.ytcdn.repositories.UserRepository;
import com.shesha4572.ytcdn.repositories.VideoFileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfileService {
    private final UserRepository userRepository;
    private final VideoFileRepository videoFileRepository;

    public ProfileDetails getProfileDetails(String displayName){
        UserApp user = userRepository.findByDisplayName(displayName).orElse(null);
        if(user == null){
            return null;
        }
        List<VideoFile> uploaded = videoFileRepository.findVideoFilesByOwnerId(user.getUsername());
        ArrayList<SearchResult> uploadedVideos = new ArrayList<>();
        uploaded.forEach(videoFile -> uploadedVideos.add(
                SearchResult.builder()
                        .internalFileId(videoFile.getInternalFileId())
                        .desc(videoFile.getDesc())
                        .thumbnailLink(videoFile.getThumbnailLink())
                        .title(videoFile.getTitle())
                        .viewCounter(videoFile.getViewCounter())
                        .uploadedOn(videoFile.getUploadedOn())
                        .ownerDisplayName(videoFile.getOwnerDisplayId())
                        .build()
        ));
        return ProfileDetails.builder()
                .name(user.getName())
                .createdOn(user.getCreatedOn())
                .displayName(user.getDisplayName())
                .uploadedVideos(uploadedVideos)
                .build();
    }
}
