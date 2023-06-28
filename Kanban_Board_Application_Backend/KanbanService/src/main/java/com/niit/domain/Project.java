/*
 * Author : Anisha Palei
 * Date : 14-03-2023
 * Created with : IntelliJ IDEA Community Edition
 */

package com.niit.domain;

import lombok.Data;

import java.util.List;

@Data
public class Project {
    private int projectId;
    private String projectName;

    private List<Task> taskList;

    public Project(int projectId, String projectName, List<Task> taskList) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.taskList = taskList;
    }

    public Project() {
    }
}
