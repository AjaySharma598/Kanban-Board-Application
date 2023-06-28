/*
 * Author : Anisha Palei
 * Date : 11-02-2023
 * Created with : IntelliJ IDEA Community Edition
 */

package com.niit.controller;

import com.niit.domain.User;
import com.niit.exception.UserAlreadyExistException;
import com.niit.exception.UserNotFoundException;
import com.niit.security.SecurityGeneratorTokenImpl;
import com.niit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    UserService userService;

    @Autowired
    SecurityGeneratorTokenImpl securityGeneratorToken;

    @PostMapping("/user")
    public ResponseEntity<?> registerUser(@RequestBody User user) throws UserAlreadyExistException {

        try{
            User newUser=userService.saveUser(user);
            return new ResponseEntity<User>(newUser, HttpStatus.CREATED);

        } catch (UserAlreadyExistException exception) {
            throw new UserAlreadyExistException();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> checkAuthentication(@RequestBody User userCheck) throws UserNotFoundException {
        try{
        User userDetail=userService.login(userCheck.getEmailId(),userCheck.getPassword());
            return new ResponseEntity(securityGeneratorToken.generateToken(userDetail), HttpStatus.OK);
        }catch (UserNotFoundException exception)
        {
            throw  new UserNotFoundException();
        }
    }
}
