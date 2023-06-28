package com.bej.notificationservice.service;


import com.bej.notificationservice.config.TaskDTO;
import com.bej.notificationservice.domain.Notification;

public interface NotificationService {
    public Notification getNotification(String emailId);

    Notification deleteTask(String emailId, String projectName, Object a);

    void saveNotifications(TaskDTO trackDTO);
}



