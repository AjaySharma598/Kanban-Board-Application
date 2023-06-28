/*
 * Author:Ajay Sharma
 * Date : 22-03-2023
 *Created With : Intellij IDEA Community Edition
 */

package com.bej.notificationservice.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
public class Project {
    @Id
    private String projectName;
    List<Object> taskList;
}
