package com.shesha4572.ytcdn.services;

import com.shesha4572.ytcdn.models.FileInitDto;
import com.shesha4572.ytcdn.models.UploadChunkDto;
import com.shesha4572.ytcdn.models.VideoFile;
import com.shesha4572.ytcdn.repositories.VideoFileRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriComponentsBuilder;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@Service
@Slf4j
public class UploadService {

    private final String MASTER_NODE_URL = System.getenv("MASTER_NODE_URL");
    private final String SLAVE_SERVICE_URL = System.getenv("SLAVE_SERVICE_URL");
    private final VideoFileRepository videoFileRepository;

    @Autowired
    public UploadService(VideoFileRepository videoFileRepository){
        this.videoFileRepository = videoFileRepository;
    }

    public ResponseEntity<?> initFileUpload(FileInitDto videoDetails , String userId , String userName){
        VideoFile videoFile = VideoFile.builder()
                .internalFileId(RandomStringUtils.randomAlphanumeric(32))
                .fileSizeBytes(videoDetails.getFileSize())
                .uploadedOn(LocalDateTime.now())
                .title(videoDetails.getTitle())
                .ownerId(userId)
                .desc(videoDetails.getDesc())
                .thumbnailLink(videoDetails.getThumbnailLink())
                .viewCounter(0)
                .ownerDisplayId(userName)
                .build();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String , Object> requestBody = new HashMap<>();
        requestBody.put("fileName" , videoFile.getTitle());
        requestBody.put("fileId" , videoFile.getInternalFileId());
        requestBody.put("fileSizeBytes" , videoFile.getFileSizeBytes());
        log.info("File #" + videoFile.getInternalFileId() + " being allocated");
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        log.info("Request Body : " + request.getBody());
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(MASTER_NODE_URL + "/api/v1/file/createFile");
        try {
            ResponseEntity<?> response = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.POST,
                    request,
                    Object.class);
            if (response.getStatusCode() == HttpStatusCode.valueOf(200)) {
                videoFileRepository.save(videoFile);
                return response;
            } else {
                log.warn("Init file creation failed " + response.getBody());
            }
        }
        catch (Exception e){
            log.error(e.getMessage());
        }
        return null;
    }

    public void uploadChunk(MultipartFile file , UploadChunkDto uploadDetails){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String , Object> requestBody = new LinkedMultiValueMap<>();
        RestTemplate restTemplate = new RestTemplate();
        String params = "";
        params = params + uploadDetails.getChunkId() + "/";
        params = params + uploadDetails.getChunkIndex() + "/";
        requestBody.add("file" , file.getResource());
        for (int i = 0; i < 3; i++) {
            HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(requestBody, headers);
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://" + uploadDetails.getPodNames().get(i) + "." + SLAVE_SERVICE_URL + "/api/v1/slave/chunk/upload/" + params + i);
            log.info("Replica #" + (i + 1) + " of file chunk #" + uploadDetails.getChunkId() + " to pod " + uploadDetails.getPodNames().get(i));
            try {
                ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(),
                        HttpMethod.POST,
                        request,
                        String.class);
                if(response.getStatusCode() == HttpStatusCode.valueOf(200)){
                    log.info("Replica #" + (i + 1) + " uploaded successfully");
                }
                else {
                    log.warn("Replica # " + (i + 1) +  " uploaded failed\n Message: " + response.getBody());
                }
            }
            catch (Exception e){
                log.error("Error : " + e.getMessage());
            }
        }
    }
}
