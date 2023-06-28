/*
 * Author : Anisha Palei
 * Date : 10-03-2023
 * Created with : IntelliJ IDEA Community Edition
 */

package com.niit.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
public class ProjectMember {

    private int memberId;
    private String memberName;
    private String memberEmailId;

    private int noOfTask;

    public ProjectMember( String memberName,String memberEmailId, int noOfTask) {

        this.memberName = memberName;
        this.memberEmailId = memberEmailId;
        this.noOfTask = noOfTask;
    }

    public ProjectMember() {
    }
}
