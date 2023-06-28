/*
 * Author : Anisha Palei
 * Date : 10-03-2023
 * Created with : IntelliJ IDEA Community Edition
 */

package com.niit.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Generated;
import java.util.Date;

@Data
public class Task {


    private int taskId;
    private  String taskName;

    private String taskDescription;

    private String priority;

    private String taskStatus;

    private Date taskDate;

    private String memberName;

    public Task(int taskId, String taskName, String taskDescription, String priority, String taskStatus, Date taskDate, String memberName) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.taskDescription = taskDescription;
        this.priority = priority;
        this.taskStatus = taskStatus;
        this.taskDate = taskDate;
        this.memberName = memberName;
    }

    public Task() {
    }
}
