/*
 * Author : Anisha Palei
 * Date : 09-03-2023
 * Created with : IntelliJ IDEA Community Edition
 */

package com.niit.domain;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document
public class User {

    @Id
    private String emailId;
    private  String password;
    private  String userName;

    private  String mobileNo;
    private List<Project> projectLists;
    private List<ProjectMember> projectMemberLists;

    public User(String emailId, String password, String userName, String mobileNo, List<Project> projectLists, List<ProjectMember> projectMemberLists) {
        this.emailId = emailId;
        this.password = password;
        this.userName = userName;
        this.mobileNo = mobileNo;
        this.projectLists = projectLists;
        this.projectMemberLists = projectMemberLists;
    }

    public User() {
    }
}
