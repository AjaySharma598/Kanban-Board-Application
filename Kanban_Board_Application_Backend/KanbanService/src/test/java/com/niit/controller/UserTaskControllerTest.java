package com.niit.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.niit.domain.Project;
import com.niit.domain.ProjectMember;
import com.niit.domain.Task;
import com.niit.domain.User;
import com.niit.exception.*;
import com.niit.rabbitMq.TaskDTO;
import com.niit.service.UserService;
import com.niit.service.UserServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserTaskControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Mock
    UserServiceImpl userService;

    @InjectMocks
    private UserTaskController userTaskController;
    private User user1, user2,user3;

    private Project project1, project2;

    private Task task1, task2, task3;
    private ProjectMember projectMember1, projectMember2;

    List<Project> projectList=new ArrayList<>();

    List<Task> taskList=new ArrayList<>();
    List<Task> taskList1=new ArrayList<>();
    List<ProjectMember> projectMemberList=new ArrayList<>();

    @BeforeEach
    void setUp() {
        projectMember1 = new ProjectMember("Tester", "testing@gmail.com", 1);
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

        mockMvc = MockMvcBuilders.standaloneSetup(userTaskController).build();
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

    @Test
    void registerUserSuccess() throws Exception {
        when(userService.saveUser(any())).thenReturn(user1);
        mockMvc.perform(post("/api/v2/register")//making dummy http post request
                        .contentType(MediaType.APPLICATION_JSON)//setting the content type of the post request
                        .content(jsonToString(user1)))//firstly, java object will be converted to json string then will
                //be passed with the mock http request
                .andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());

        verify(userService, Mockito.times(1)).saveUser(any());
    }
    @Test
    void registerUserFailure() throws Exception {
        when(userService.saveUser(any())).thenReturn(user1);
        mockMvc.perform(post("/api/v2/register")//making dummy http post request
                        .contentType(MediaType.APPLICATION_JSON)//setting the content type of the post request
                        .content(jsonToString(user1)))//firstly, java object will be converted to json string then will
                //be passed with the mock http request
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());

        verify(userService, Mockito.times(1)).saveUser(any());
    }
    @Test
    void createProjectSuccess() throws Exception {
        when(userService.createProject(anyString(),any())).thenReturn(user3);
        mockMvc.perform(post("/api/v2/secure/createProject/{emailId}", user3.getEmailId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(project1)))
                .andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).createProject(anyString(),any());
    }
    @Test
    void createProjectFailure() throws Exception {
        when(userService.createProject(anyString(),any())).thenReturn(user3);
        mockMvc.perform(post("/api/v2/secure/createProject/{emailId}", user3.getEmailId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(project1)))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).createProject(anyString(),any());
    }

    @Test
    void getAllProjectsSuccess() throws Exception {
        when(userService.getAllProjects(anyString())).thenReturn(projectList);
        mockMvc.perform(get("/api/v2/secure/projects/{userEmail}",user1.getEmailId()))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).getAllProjects(anyString());
    }
    @Test
    void getAllProjectsFailure() throws Exception {
        when(userService.getAllProjects(anyString())).thenReturn(projectList);
        mockMvc.perform(get("/api/v2/secure/projects/{userEmail}",user1.getEmailId()))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).getAllProjects(anyString());
    }

    @Test
    void deleteProjectSuccess() throws Exception {
        when(userService.deleteProject(anyString(),anyInt())).thenReturn(user1);
        mockMvc.perform(delete("/api/v2/secure/deleteProject/{emailId}/{projectId}",
                        user1.getEmailId(),project1.getProjectId()))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).deleteProject(anyString(),anyInt());
    }
    @Test
    void deleteProjectFailure() throws Exception {
        when(userService.deleteProject(anyString(),anyInt())).thenReturn(user1);
        mockMvc.perform(delete("/api/v2/secure/deleteProject/{emailId}/{projectId}",
                        user1.getEmailId(),project1.getProjectId()))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).deleteProject(anyString(),anyInt());
    }
     @Test
    void addProjectMemberSuccess() throws Exception {
         when(userService.addProjectMember(anyString(),any())).thenReturn(user1);
         mockMvc.perform(post("/api/v2/secure/addProjectMember/{emailId}",user1.getEmailId())
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(jsonToString(projectMember2)))
                 .andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
         verify(userService, Mockito.times(1)).addProjectMember(anyString(),any());
    }
    @Test
    void addProjectMemberFailure() throws Exception {
        when(userService.addProjectMember(anyString(),any())).thenReturn(user1);
        mockMvc.perform(post("/api/v2/secure/addProjectMember/{emailId}",user1.getEmailId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(projectMember2)))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).addProjectMember(anyString(),any());
    }
    @Test
    void getAllProjectMembersSuccess() throws Exception {
        when(userService.getAllProjectMembers(anyString())).thenReturn(projectMemberList);
        mockMvc.perform(get("/api/v2/secure/projectMembers/{emailId}",user1.getEmailId()))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).getAllProjectMembers(anyString());
    }
    @Test
    void getAllProjectMembersFailure() throws Exception {
        when(userService.getAllProjectMembers(anyString())).thenReturn(projectMemberList);
        mockMvc.perform(get("/api/v2/secure/projectMembers/{emailId}",user1.getEmailId()))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).getAllProjectMembers(anyString());
    }
    @Test
    void deleteProjectMemberSuccess() throws Exception {
        when(userService.deleteProjectMember(anyString(),anyInt())).thenReturn(user1);
        mockMvc.perform(delete("/api/v2/secure/deleteProjectMember/{emailId}/{projectId}",
                        user1.getEmailId(),project1.getProjectId()))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).deleteProjectMember(anyString(),anyInt());
    }
    @Test
    void deleteProjectMemberFailure() throws Exception {
        when(userService.deleteProjectMember(anyString(),anyInt())).thenReturn(user1);
        mockMvc.perform(delete("/api/v2/secure/deleteProjectMember/{emailId}/{projectId}",
                        user1.getEmailId(),project1.getProjectId()))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).deleteProjectMember(anyString(),anyInt());
    }
    @Test
    void createTaskSuccess() throws Exception {
        when(userService.createTask(anyString(),anyString(),any())).thenReturn(user1);
        mockMvc.perform(post("/api/v2/secure/createTask/{emailId}/{projectName}",
                        user1.getEmailId(),project1.getProjectName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(task1)))
                .andExpect(status().isCreated()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).createTask(anyString(),anyString(),any());
    }
    @Test
    void createTaskFailure() throws Exception {
        when(userService.createTask(anyString(),anyString(),any())).thenReturn(user1);
        mockMvc.perform(post("/api/v2/secure/createTask/{emailId}/{projectName}",
                        user1.getEmailId(),project1.getProjectName())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(task1)))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).createTask(anyString(),anyString(),any());
    }
    @Test
    void getAllTaskSuccess() throws Exception {
        when(userService.getAllTask(anyString(),anyString())).thenReturn(taskList);
        mockMvc.perform(get("/api/v2/secure/getTasks/{emailId}/{projectName}",
                        user1.getEmailId(),project1.getProjectName()))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).getAllTask(anyString(),anyString());
    }
    @Test
    void getAllTaskFailure() throws Exception {
        when(userService.getAllTask(anyString(),anyString())).thenReturn(taskList);
        mockMvc.perform(get("/api/v2/secure/getTasks/{emailId}/{projectName}",
                        user1.getEmailId(),project1.getProjectName()))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).getAllTask(anyString(),anyString());
    }

    @Test
    void getTaskByIdSuccess() throws Exception {
        when(userService.getTask(anyString(),anyString(),anyInt())).thenReturn(task1);
        mockMvc.perform(get("/api/v2/secure/getTaskById/{emailId}/{projectName}/{taskId}",
                        user1.getEmailId(),project1.getProjectName(),task1.getTaskId()))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).getTask(anyString(),anyString(),anyInt());
    }
    @Test
    void getTaskByIdFailure() throws Exception {
        when(userService.getTask(anyString(),anyString(),anyInt())).thenReturn(task1);
        mockMvc.perform(get("/api/v2/secure/getTaskById/{emailId}/{projectName}/{taskId}",
                        user1.getEmailId(),project1.getProjectName(),task1.getTaskId()))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).getTask(anyString(),anyString(),anyInt());
    }
    @Test
    void updateTaskSuccess() throws Exception {
        when(userService.updateTask(anyString(),anyString(),any(),anyInt())).thenReturn(user1);
        mockMvc.perform(put("/api/v2/secure/updateTask/{emailId}/{projectName}/{taskId}",
                        user1.getEmailId(),project1.getProjectName(),task1.getTaskId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(task1)))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).updateTask(anyString(),anyString(),any(),anyInt());
    }
    @Test
    void updateTaskFailure() throws Exception {
        when(userService.updateTask(anyString(),anyString(),any(),anyInt())).thenReturn(user1);
        mockMvc.perform(put("/api/v2/secure/updateTask/{emailId}/{projectName}/{taskId}",
                        user1.getEmailId(),project1.getProjectName(),task1.getTaskId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonToString(task1)))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).updateTask(anyString(),anyString(),any(),anyInt());
    }
    @Test
    void deleteTaskSuccess() throws Exception {
        when(userService.deleteTask(anyString(),anyString(),anyInt())).thenReturn(user1);
        mockMvc.perform(delete("/api/v2/secure/deleteTask/{emailId}/{projectName}/{taskId}",
                        user1.getEmailId(),project1.getProjectName(),task1.getTaskId()))
                .andExpect(status().isOk()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).deleteTask(anyString(),anyString(),anyInt());
    }
    @Test
    void deleteTaskFailure() throws Exception {
        when(userService.deleteTask(anyString(),anyString(),anyInt())).thenReturn(user1);
        mockMvc.perform(delete("/api/v2/secure/deleteTask/{emailId}/{projectName}/{taskId}",
                        user1.getEmailId(),project1.getProjectName(),task1.getTaskId()))
                .andExpect(status().isConflict()).andDo(MockMvcResultHandlers.print());
        verify(userService, Mockito.times(1)).deleteTask(anyString(),anyString(),anyInt());
    }
    private static String jsonToString(final Object ob) throws JsonProcessingException {
        String result;
        try {
            ObjectMapper mapper = new ObjectMapper();
            String jsonContent = mapper.writeValueAsString(ob);
            result = jsonContent;
        } catch(JsonProcessingException e) {
            result = "JSON processing error";
        }

        return result;
    }
}