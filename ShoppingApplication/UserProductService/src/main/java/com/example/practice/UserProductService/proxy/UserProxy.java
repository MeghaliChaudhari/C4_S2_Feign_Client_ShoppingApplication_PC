package com.example.practice.UserProductService.proxy;

import com.example.practice.UserProductService.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-authentication-service",url = "http://user-authentication-service:8082")
public interface UserProxy {

    @PostMapping("/userauth/register")
    public ResponseEntity<?> saveUser(@RequestBody User user);
}
