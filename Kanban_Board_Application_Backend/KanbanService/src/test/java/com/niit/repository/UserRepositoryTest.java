package com.niit.repository;

import com.niit.domain.Project;
import com.niit.domain.ProjectMember;
import com.niit.domain.Task;
import com.niit.domain.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private User user1;

    private Project project1;
    private Task task1;
    private ProjectMember projectMember1;
    List<Project> projectList=new ArrayList<>();
    List<Task> taskList=new ArrayList<>();

    List<ProjectMember> projectMemberList=new ArrayList<>();

    @BeforeEach
    void setUp() {
        projectMember1 = new ProjectMember("Tester", "testing@gmail.com", 1);
        projectMemberList.add(projectMember1);
        task1 = new Task(1,"task1","Create website","High","ToDo",null,"Tester");
        taskList.add(task1);
        project1=new Project(1,"Project1",taskList);

        user1=new User("testing.com","Pass@123","Tester","8559634068",projectList,projectMemberList);

    }

    @AfterEach
    void tearDown() {
        user1=null;
        task1=null;
        project1=null;
        projectMember1=null;
    }

    @Test
    public void saveUser()
    {
        userRepository.save(user1);
        User user2=userRepository.findById(user1.getEmailId()).get();
        assertNotNull(user2);
        assertEquals(user1.getEmailId(),user2.getEmailId());
    }
}