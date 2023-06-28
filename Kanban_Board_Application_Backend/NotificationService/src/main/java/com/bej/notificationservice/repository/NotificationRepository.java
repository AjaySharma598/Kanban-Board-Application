package com.bej.notificationservice.repository;


import com.bej.notificationservice.domain.Notification;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface NotificationRepository extends MongoRepository<Notification,String> {


}
