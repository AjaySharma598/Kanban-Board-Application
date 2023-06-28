package com.niit.service;

import com.niit.domain.Project;
import com.niit.domain.ProjectMember;
import com.niit.domain.Task;
import com.niit.domain.User;
import com.niit.exception.*;
import com.niit.rabbitMq.TaskDTO;
import com.niit.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DataMongoTest
class UserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private TaskDTO taskDTO;
    private User user1, user2,user3;

    private Project project1, project2;

    private Task task1, task2, task3;
    private ProjectMember projectMember1, projectMember2;

    List<User> userList;
    List<Project> projectList=new ArrayList<>();

    List<Task> taskList=new ArrayList<>();
    List<Task> taskList1=new ArrayList<>();
    List<ProjectMember> projectMemberList=new ArrayList<>();

    @BeforeEach
    void setUp() {
        projectMember1 = new ProjectMember("Tester", "testing@gmail.com", 1);
        projectMember2 = new ProjectMember("ManualTester", "testing1@gmail.com", 1);
        projectMemberList.add(projectMember1);
        task1 = new Task(1,"task1","Create website","High","To Do",new Date(2023, 03, 21),"Tester");
        task2 = new Task(2,"task2","Design UI","medium","In Progress",new Date(2023, 03, 20),"abc");
        task3 = new Task(3,"task3","Create a popUp In frontend","low","Completed",new Date(2023, 03, 19),"abc");
        taskList.add(task1); taskList1.add(task3);
        project1=new Project(1,"Project1",taskList);
        project2=new Project(2,"Project2",taskList1);
          projectList.add(project1);

        user1=new User("testing.com","Pass@123","Tester","8559634068",projectList,projectMemberList);
        user2=new User("testing1.com","Pass@124","ManualTester","8559634035",projectList,projectMemberList);
        user3=new User("testing2.com","Pass@124","Tester2","8689634035",projectList,projectMemberList);
    }

    @AfterEach
    void tearDown() {
        user1=null;
        user2=null;
        user3=null;
        task1=null;
        task2=null;
        task3=null;
        project1=null;
        project2=null;
        projectMember1=null;
        projectMember2=null;
    }

//    @Test
//    void saveUser() throws UserNotFoundException, UserAlreadyExistException, ProjectMemberAlreadyExistException {
//        when(userRepository.findById(user3.getEmailId())) .thenReturn(Optional.ofNullable(null));
//        when(userRepository.save(any())).thenReturn(user3);
//        assertEquals(user3,userService.saveUser(user3));
//        verify(userRepository,times(1)).save(any());
//        verify(userRepository,times(1)).findById(anyString());
//    }

    @Test
    void createProject() throws UserNotFoundException, ProjectAlreadyExistException {
        when(userRepository.findById(user1.getEmailId())).thenReturn(Optional.of(user1));
        when(userRepository.save(any())).thenReturn(user1);
        assertEquals(user1,userService.createProject(user1.getEmailId(),project2));
        verify(userRepository,times(1)).save(any());
    }

    @Test
    void getAllProjects() throws UserNotFoundException {
        when(userRepository.findById(user1.getEmailId())).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2.getEmailId())).thenReturn(Optional.ofNullable(null));
        assertThrows(UserNotFoundException.class,()->userService.getAllProjects(user2.getEmailId()));
        assertEquals(projectList,userService.getAllProjects(user1.getEmailId()));
        verify(userRepository,times(3)).findById(anyString());
    }

    @Test
    void deleteProject() throws UserNotFoundException {
        when(userRepository.findById(user1.getEmailId())).thenReturn(Optional.of(user1));
        when(userRepository.findById(user2.getEmailId())).thenReturn(Optional.ofNullable(null));
        when(userRepository.save(any())).thenReturn(user1);
        assertThrows(UserNotFoundException.class,()->userService.deleteProject(user2.getEmailId(),project1.getProjectId()));
        assertEquals(user1,userService.deleteProject(user1.getEmailId(),project1.getProjectId()));
        verify(userRepository,times(1)).save(any());
//        verify(userRepository,times(4)).findById(anyString());
    }



    @Test
    void addProjectMember() throws UserNotFoundException, ProjectMemberAlreadyExistException {
        when(userRepository.findById(user1.getEmailId())).thenReturn(Optional.of(user1));
        when(userRepository.save(any())).thenReturn(user1);
        assertEquals(user1,userService.addProjectMember(user1.getEmailId(),projectMember2));
        verify(userRepository,times(1)).save(any());
        verify(userRepository,times(2)).findById(anyString());
    }

    @Test
    void getAllProjectMembers() throws UserNotFoundException {
        when(userRepository.findById(user1.getEmailId())).thenReturn(Optional.of(user1));
        assertEquals(projectMemberList,userService.getAllProjectMembers(user1.getEmailId()));
        verify(userRepository,times(2)).findById(anyString());
    }

    @Test
    void deleteProjectMember() throws UserNotFoundException {
        when(userRepository.findById(user1.getEmailId())).thenReturn(Optional.of(user1));
        when(userRepository.save(any())).thenReturn(user1);
        assertEquals(user1,userService.deleteProjectMember(user1.getEmailId(),projectMember1.getMemberId()));
        verify(userRepository,times(1)).save(any());
    }

    @Test
    void createTask() throws UserNotFoundException {
//        when(userRepository.findById(user1.getEmailId())).thenReturn(Optional.of(user1));
//        when(userRepository.save(any())).thenReturn(user1);
//        assertEquals(user1,userService.createTask(user1.getEmailId(),project1.getProjectName(),task3));
//        verify(userRepository,times(1)).save(any());
    }

    @Test
    void getAllTask() throws UserNotFoundException{
        when(userRepository.findById(user1.getEmailId())).thenReturn(Optional.of(user1));
        assertEquals(taskList.size(),userService.getAllTask(user1.getEmailId(),project1.getProjectName()).size());

    }


    @Test
    void updateTask() throws UserNotFoundException {
        when(userRepository.findById(user1.getEmailId())).thenReturn(Optional.of(user1));
        when(userRepository.save(any())).thenReturn(user1);
        assertEquals(user1,userService.updateTask(user1.getEmailId(),project1.getProjectName(),task2, task2.getTaskId()));
        verify(userRepository,times(1)).save(any());
        verify(userRepository,times(2)).findById(anyString());
    }

    @Test
    void deleteTask() throws UserNotFoundException, ProjectNotFoundException, TaskNotFoundException {
        when(userRepository.findById(user1.getEmailId())).thenReturn(Optional.of(user1));
        when(userRepository.save(any())).thenReturn(user1);
        assertEquals(user1,userService.deleteTask(user1.getEmailId(),project1.getProjectName(),task1.getTaskId()));
        verify(userRepository,times(1)).save(any());
        verify(userRepository,times(2)).findById(anyString());
    }
}