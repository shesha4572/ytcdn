package com.shesha4572.ytcdn.controllers;

import com.shesha4572.ytcdn.configs.SecureController;
import com.shesha4572.ytcdn.models.FileInitDto;
import com.shesha4572.ytcdn.models.UploadChunkDto;
import com.shesha4572.ytcdn.models.UserApp;
import com.shesha4572.ytcdn.services.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/upload")
@RequiredArgsConstructor
public class UploadController implements SecureController {

    private final UploadService uploadService;

    @PostMapping("/initFile")
    public ResponseEntity<?> initFile(@RequestBody FileInitDto fileInitDetails){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserApp user = (UserApp) authentication.getPrincipal();
        String ownerId = user.getUsername();
        ResponseEntity<?> responseEntity = uploadService.initFileUpload(fileInitDetails , ownerId);
        if(responseEntity == null){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }

    @PostMapping(value = "/uploadChunk/{chunkId}/{chunkIndex}/{podNames}" , consumes = "multipart/form-data")
    public ResponseEntity<?> uploadChunk(@RequestParam("file") MultipartFile file , @PathVariable String chunkId , @PathVariable String chunkIndex , @PathVariable List<String> podNames){
        UploadChunkDto uploadDetails = new UploadChunkDto();
        uploadDetails.setChunkId(chunkId);
        uploadDetails.setChunkIndex(Integer.parseInt(chunkIndex));
        uploadDetails.setPodNames(podNames);
        uploadService.uploadChunk(file , uploadDetails);
        return ResponseEntity.ok().build();
    }
}
