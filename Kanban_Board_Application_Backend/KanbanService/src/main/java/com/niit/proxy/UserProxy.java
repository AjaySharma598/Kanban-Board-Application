package com.niit.proxy;

import com.niit.domain.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-authentication-service",url="localhost:8084")
public interface UserProxy {
    @PostMapping("/api/v1/user")
    public ResponseEntity saveUserInAuth(@RequestBody User user);
}
