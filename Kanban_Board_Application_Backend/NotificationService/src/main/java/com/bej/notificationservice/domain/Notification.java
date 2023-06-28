package com.bej.notificationservice.domain;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class Notification {
    @Id
    private String emailId;//userEmailId
    private String notificationMessage;//Notification Message

    private List<Project> projectLists;


}





