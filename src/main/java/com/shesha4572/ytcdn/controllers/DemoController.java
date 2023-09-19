package com.shesha4572.ytcdn.controllers;

import com.shesha4572.ytcdn.configs.SecureController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController("/api/v1/demo")
public class DemoController implements SecureController {

    @GetMapping("/")
    public ResponseEntity<?> ping(){
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> aboutMe(HttpServletRequest request){
        HashMap<String , String> response = new HashMap<>();
        response.put("user" , request.getUserPrincipal().getName());
        return ResponseEntity.ok().body(response);
    }
}
