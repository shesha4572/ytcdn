package com.shesha4572.ytcdn.controllers;

import com.shesha4572.ytcdn.configs.SecureController;
import com.shesha4572.ytcdn.models.UserApp;
import com.shesha4572.ytcdn.services.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/v1/like")
@RequiredArgsConstructor
public class LikeController implements SecureController {

    private final LikeService likeService;

    @PostMapping("/{internalFileId}")
    public ResponseEntity<?> likeVideo(@PathVariable String internalFileId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserApp user = (UserApp) authentication.getPrincipal();
        if(likeService.likeVideo(internalFileId , user.getUsername())){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.internalServerError().build();
    }

    @GetMapping("/hasLiked/{internalFileId}")
    public ResponseEntity<?> hasLikedVideo(@PathVariable String internalFileId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserApp user = (UserApp) authentication.getPrincipal();
        if(likeService.hasLikedVideo(internalFileId , user.getUsername())){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

}
