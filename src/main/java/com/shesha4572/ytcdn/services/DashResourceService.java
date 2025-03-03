package com.shesha4572.ytcdn.services;

import org.springframework.core.io.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashResourceService {
    private final String FILE_SERVER_URL = System.getenv("FILE_SERVER_URL");

    public ResponseEntity<Resource> getWholeFile(String resourceId){
        log.info("Serving file #{}" , resourceId);
        RestTemplate restTemplate = new RestTemplate();
        String uri = FILE_SERVER_URL + "/read/" + resourceId;
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(uri);
        try {
            Resource requiredFile;
            ResponseEntity<Resource> response = restTemplate.exchange(builder.toUriString() , HttpMethod.GET , null , Resource.class);
            if(response.getStatusCode() == HttpStatusCode.valueOf(200)){
                requiredFile = Objects.requireNonNull(response.getBody());
                log.info("Downloaded #" + resourceId + " successfully");
                return ResponseEntity.ok().contentLength(requiredFile.contentLength()).body(requiredFile);
            }
            else {
                log.info("Error Downloading File #" + resourceId + "\nError : " + response.getStatusCode());
                return ResponseEntity.internalServerError().build();
            }
        }
        catch (Exception e){
            log.error("Error : " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
