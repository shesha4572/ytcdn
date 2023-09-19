package com.shesha4572.ytcdn.controllers;

import com.shesha4572.ytcdn.models.AuthenticationRequest;
import com.shesha4572.ytcdn.models.AuthenticationResponse;
import com.shesha4572.ytcdn.models.RegisterRequest;
import com.shesha4572.ytcdn.services.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) throws Exception{
        try {
            return ResponseEntity.ok(authenticationService.register(request));
        }
        catch (Exception e){
            HashMap<String , String> response = new HashMap<>();
            response.put("status" , "FAIL");
            response.put("message" , e.getMessage());
            response.put("stack" , Arrays.toString(e.getStackTrace()));
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request) throws Exception {
        try {
            return ResponseEntity.ok(authenticationService.login(request));
        }
        catch (Exception e){
            HashMap<String , String> response = new HashMap<>();
            response.put("status" , "FAIL");
            response.put("message" , e.getMessage());
            response.put("stack" , Arrays.toString(e.getStackTrace()));
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
        }
    }

}
