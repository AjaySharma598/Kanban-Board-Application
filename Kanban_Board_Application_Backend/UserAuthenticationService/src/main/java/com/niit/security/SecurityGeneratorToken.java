package com.niit.security;

import com.niit.domain.User;

import java.util.Map;

public interface SecurityGeneratorToken {
    public Map<String,String> generateToken(User userDetail);
}
