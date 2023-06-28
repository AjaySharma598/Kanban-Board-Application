package com.niit.service;

import com.niit.domain.User;
import com.niit.exception.UserAlreadyExistException;
import com.niit.exception.UserNotFoundException;

public interface UserService {

    public User saveUser(User user) throws UserAlreadyExistException;
    public User login(String emailId , String password) throws UserNotFoundException;
}
