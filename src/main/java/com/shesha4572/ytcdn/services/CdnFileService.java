package com.shesha4572.ytcdn.services;


import com.shesha4572.ytcdn.models.Chunk;
import com.shesha4572.ytcdn.models.FileInfo;
import com.shesha4572.ytcdn.repositories.FileInfoRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@Slf4j
@RequiredArgsConstructor
public class CdnFileService {

    private final FileInfoRedisRepository fileInfoRedisRepository;
    private final String MASTER_NODE_URL = System.getenv("MASTER_NODE_URL");
    private final String SLAVE_SERVICE_URL = System.getenv("SLAVE_SERVICE_URL");

    @SneakyThrows
    public ResponseEntity<Resource> getVideoRange(String videoId, long startIndex , long endIndex) {
        if (!fileInfoRedisRepository.existsById(videoId)) {
            RestTemplate restTemplate = new RestTemplate();
            String uri = MASTER_NODE_URL + "/api/v1/file/getAllFileChunks/" + videoId;
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
            log.info("Cache miss for File #" + videoId);
            try {
                ResponseEntity<FileInfo> response = restTemplate.exchange(builder.toUriString() , HttpMethod.GET , null , FileInfo.class);
                if(response.getStatusCode() == HttpStatusCode.valueOf(200)){
                    fileInfoRedisRepository.save(Objects.requireNonNull(response.getBody()));
                    log.info("Cached File #" + videoId + " successfully");
                }
                else {
                    log.info("Error caching File #" + videoId + "\nError : " + response.getStatusCode());
                }
            }
            catch (Exception e){
                log.error("Error : " + e.getMessage());
                return null;
            }
        }
            int chunkIndex = (int) (startIndex / 64000000);
            int startIndexRelative = (int) (startIndex % 64000000);
            FileInfo fileInfo = fileInfoRedisRepository.findById(videoId).get();
            Chunk requiredChunk = null;
            int remainingBytes = 64000000 - startIndexRelative;
            for (int i = 0; i < fileInfo.getChunkList().size(); i++) {
                if(chunkIndex == fileInfo.getChunkList().get(i).getChunkIndex()){
                    requiredChunk = fileInfo.getChunkList().get(i);
                    break;
                }
            }
            assert requiredChunk != null;
            if(requiredChunk.getChunkIndex() == fileInfo.getChunkList().size() - 1){
                int lastChunkSize = (int) (fileInfo.getSize() % 64000000);
                remainingBytes = lastChunkSize - startIndexRelative;
            }
            int endIndexRelative = (int) (startIndexRelative + (endIndex - startIndex));
            if (endIndex == -1) {
                endIndexRelative = remainingBytes - 1 + startIndexRelative;
            }
            if (endIndexRelative > remainingBytes) {
                endIndexRelative = remainingBytes - 1 + startIndexRelative;
            }
            log.info("Reading video #" + videoId + " Chunk #" + requiredChunk.getChunkId() + " Chunk Index #" + chunkIndex +  " Start = " + startIndexRelative + " End = " + endIndexRelative);
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
            Map<String , Object> body = new HashMap<>();
            body.put("fileChunkId" , requiredChunk.getChunkId());
            body.put("startIndex" , startIndexRelative);
            body.put("endIndex" , endIndexRelative);
            HttpEntity<Map<String , Object>> request = new HttpEntity<>(body , httpHeaders);
            Resource requiredBytes = null;
            if(requiredChunk.getReplicaPodList() == null){
                fileInfoRedisRepository.delete(fileInfo);
                log.warn("Chunk has no replicas available!");
                return ResponseEntity.notFound().build();
            }
            for (int i = 0; i < requiredChunk.getReplicaPodList().size(); i++) {
                UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("http://" + requiredChunk.getReplicaPodList().get(i) + "." + SLAVE_SERVICE_URL + "/api/v1/slave/chunk/getPartialChunk");
                ResponseEntity<Resource> response = restTemplate.exchange(builder.toUriString() , HttpMethod.POST , request , Resource.class);
                if(response.getStatusCode() == HttpStatusCode.valueOf(200)){
                    requiredBytes = Objects.requireNonNull(response.getBody());
                    break;
                }
            }
            assert requiredBytes != null;
            log.info(requiredBytes.contentLength() + " bytes read");
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_RANGE, "bytes " + startIndex + "-" + ((startIndex + requiredBytes.contentLength() - 1)) + "/" + fileInfo.getSize());
            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .headers(headers)
                    .contentLength(requiredBytes.contentLength())
                    .body(requiredBytes);
    }
}
