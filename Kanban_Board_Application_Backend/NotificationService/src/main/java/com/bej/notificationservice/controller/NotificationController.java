package com.bej.notificationservice.controller;

import com.bej.notificationservice.domain.Notification;
import com.bej.notificationservice.service.NotificationService;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
//@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/api/v3")
public class NotificationController {
    private NotificationService notificationService;
    @Autowired
    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/notifications/{emailId}")
    public ResponseEntity<?> notifications(@PathVariable String emailId){
        System.out.println("email " + emailId);
        return new ResponseEntity<>(notificationService.getNotification(emailId),HttpStatus.OK);
    }

    @DeleteMapping("/notification/{emailId}/{projectName}")
    public ResponseEntity<?> deleteNotification(@PathVariable String emailId,@PathVariable String projectName,@RequestBody Object a){
        Notification notification = notificationService.deleteTask(emailId, projectName, a);
        return new ResponseEntity<>(notification,HttpStatus.OK);
    }
}
