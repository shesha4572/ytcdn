package com.shesha4572.ytcdn.controllers;

import com.shesha4572.ytcdn.models.ProfileDetails;
import com.shesha4572.ytcdn.services.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final ProfileService profileService;
    @GetMapping("/user/{displayName}")
    public ResponseEntity<ProfileDetails> getProfileDetails(@PathVariable String displayName){
        ProfileDetails profileDetails = profileService.getProfileDetails(displayName);
        if(profileDetails == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().body(profileDetails);
    }
}
