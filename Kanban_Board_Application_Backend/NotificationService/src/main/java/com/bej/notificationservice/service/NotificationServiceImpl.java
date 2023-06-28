package com.bej.notificationservice.service;


import com.bej.notificationservice.config.TaskDTO;
import com.bej.notificationservice.domain.Notification;
import com.bej.notificationservice.domain.Project;
import com.bej.notificationservice.repository.NotificationRepository;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;


@Service
public class NotificationServiceImpl implements NotificationService {
    private NotificationRepository notificationRepository;

    @Autowired
    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public Notification getNotification(String emailId)
    {
        return notificationRepository.findById(emailId).get();
    }

    @Override
    public Notification deleteTask(String emailId, String projectName, Object a) {
        Notification notification = notificationRepository.findById(emailId).get();
        List<Project> projectLists = notification.getProjectLists();
        Project project1=new Project();
        for ( Project project: projectLists ) {
            if(project.getProjectName().equals(projectName)){
                project1=project;
                break;
            }
        }
        List<Object> taskList1 = project1.getTaskList();
        taskList1.remove(a);
        return  notificationRepository.save(notification);
    }

    @RabbitListener(queues = "direct-Queue")
    @Override
    public void saveNotifications(TaskDTO taskDTO) {
        int count=0;
        Notification notification = new Notification();
        String emailId = taskDTO.getJsonObject().get("emailId").toString();
        String projectName = taskDTO.getJsonObject().get("projectName").toString();
        Project project=new Project();
        if (notificationRepository.findById(emailId).isEmpty()) {
            notification.setEmailId(emailId);
            notification.setNotificationMessage("New task has been assigned to you");
            project.setProjectName(projectName);
            project.setTaskList(Arrays.asList(taskDTO.getJsonObject().get("taskData")));
            notification.setProjectLists(Arrays.asList(project));
        }else{
            notification = notificationRepository.findById(emailId).get();
            List<Project> projectLists = notification.getProjectLists();
            for ( Project project1: projectLists ) {
                if(project1.getProjectName().equals(projectName)){
                    project=project1;
                    count++;
                    break;
                }
            }
            if(count==1) {
                List<Object> taskList = project.getTaskList();
                taskList.add(taskDTO.getJsonObject().get("taskData"));
            }else {
                project.setProjectName(projectName);
                project.setTaskList(Arrays.asList(taskDTO.getJsonObject().get("taskData")));
                projectLists.add(project);
                notification.setProjectLists(projectLists);
            }
        }
        notificationRepository.save(notification);
    }
}

