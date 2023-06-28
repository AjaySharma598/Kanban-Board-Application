/*
 * Author : Anisha Palei
 * Date : 11-02-2023
 * Created with : IntelliJ IDEA Community Edition
 */

package com.niit.service;

import com.niit.domain.User;
import com.niit.exception.UserAlreadyExistException;
import com.niit.exception.UserNotFoundException;
import com.niit.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepo userRepo;

    @Override
    public User saveUser(User user) throws UserAlreadyExistException {
        if(userRepo.findById(user.getEmailId()).isPresent())
        {
            throw new UserAlreadyExistException();
        }
        return userRepo.save(user);
    }

    @Override
    public User login(String emailId, String password) throws UserNotFoundException {
        User loginUser = userRepo.findByEmailIdAndPassword(emailId, password);
        if (loginUser == null) {
            throw new UserNotFoundException();
        }
        return loginUser;
    }
}