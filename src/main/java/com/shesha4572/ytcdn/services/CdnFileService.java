package com.shesha4572.ytcdn.services;


import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.io.RandomAccessFile;

@Service
@Slf4j
public class CdnFileService {
    @SneakyThrows
    public ResponseEntity<Resource> getVideoRange(String videoId, long startIndex , long endIndex) {

        RandomAccessFile file = new RandomAccessFile("/Users/shesha4572/IdeaProjects/ytcdn/src/main/java/com/shesha4572/ytcdn/services/Don't Waste your Money - $30 vs $10,000 Racing Setup.mp4", "r");
        if(endIndex == -1){
            endIndex = startIndex + 1000000;
        }
        if(endIndex > file.length()){
            endIndex = file.length() - 1;
        }
        log.info("Reading video #" + videoId + " Start = " + startIndex + " End = " + endIndex);
        byte[] bytes = new byte[(int) (endIndex - startIndex + 1)];
        file.seek(startIndex);
        int len = file.read(bytes , 0 , bytes.length);
        log.info(len + " bytes read");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_RANGE , "bytes " + startIndex + "-" + endIndex + "/" + file.length());
        return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .headers(headers)
                .contentLength(len)
                .body(new ByteArrayResource(bytes));
    }
}
