package com.shesha4572.ytcdn.services;

import com.shesha4572.ytcdn.configs.JwtService;
import com.shesha4572.ytcdn.models.*;
import com.shesha4572.ytcdn.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(RegisterRequest request) throws Exception{
        var userApp = UserApp
                .builder()
                .displayName(request.getDisplayName())
                .DOB(request.getDOB())
                .createdOn(LocalDateTime.now())
                .name(request.getName())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();

        if (userRepository.existsByUsernameEquals(request.getUsername())) {
            throw new Exception("User with email already exists");
        }
        userRepository.save(userApp);
        var jwt = jwtService.generateJwt(userApp);
        return AuthenticationResponse
                .builder()
                .token(jwt)
                .build();
    }

    public AuthenticationResponse login(AuthenticationRequest request) throws Exception{
        if(!userRepository.existsByUsernameEquals(request.getUsername())){
            throw new UsernameNotFoundException("User doesnt exist");
        }
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        UserDetails user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        var jwt = jwtService.generateJwt(user);
        return AuthenticationResponse
                .builder()
                .token(jwt)
                .build();
    }
}
