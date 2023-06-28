package com.niit.service;

import com.niit.domain.Project;
import com.niit.domain.ProjectMember;
import com.niit.domain.Task;
import com.niit.domain.User;
import com.niit.exception.*;

import java.util.List;

public interface UserService {

    public User saveUser(User userDetail) throws UserAlreadyExistException, UserNotFoundException, ProjectMemberAlreadyExistException;
    User getUser(String emailId) throws UserNotFoundException;

//    public User addTaskToList(Task task,String emailId) throws UserNotFoundException;
//
//    public User updateTaskByUser(Task task, String emailId, int taskId) throws UserNotFoundException, TaskNotFoundException;
//
//    public boolean deleteTaskByUser(String emailId, int taskId) ;
//
//    List<Task> getAllTasks(String emailId) throws UserNotFoundException;

//  Project Methods
    User createProject(String emailId, Project project) throws UserNotFoundException, ProjectAlreadyExistException;
    List<Project> getAllProjects(String emailId) throws UserNotFoundException;
    User deleteProject(String emailId, int projectId) throws UserNotFoundException;

    Project getProject(String emailId,int projectId) throws UserNotFoundException;

    //ProjectMembers methods
    User addProjectMember(String emailId, ProjectMember projectMember) throws UserNotFoundException, ProjectMemberAlreadyExistException;

    List<ProjectMember> getAllProjectMembers(String emailId) throws UserNotFoundException;

    User deleteProjectMember(String emailId,int memberId) throws UserNotFoundException;


    //Tasks methods
    User createTask(String emailId, String projectName, Task task) throws UserNotFoundException, ProjectNotFoundException, TaskNotFoundException;
    List<Task> getAllTask(String emailId,String projectName) throws UserNotFoundException, ProjectNotFoundException;

    Task getTask(String emailId,String projectName,int taskId) throws UserNotFoundException, ProjectNotFoundException, TaskNotFoundException;

    User updateTask(String emailId, String projectName, Task task, int taskId) throws UserNotFoundException, ProjectNotFoundException, TaskNotFoundException;

    User deleteTask(String emailId, String projectName,  int  taskId) throws UserNotFoundException, ProjectNotFoundException, TaskNotFoundException;
}
