package com.shesha4572.ytcdn.services;

import com.shesha4572.ytcdn.repositories.UserRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserAppService {

    private final UserRepository userRepository;

    public UserDetails loadByUserName(String userEmail) throws UsernameNotFoundException {
        return userRepository.findByUsername(userEmail).orElseThrow(() -> new UsernameNotFoundException("No user with provided username"));
    }
}
