/*
 * Author : Anisha Palei
 * Date : 10-03-2023
 * Created with : IntelliJ IDEA Community Edition
 */

package com.niit.controller;

import com.niit.domain.Project;
import com.niit.domain.ProjectMember;
import com.niit.domain.Task;
import com.niit.domain.User;
import com.niit.exception.*;
import com.niit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
//@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/api/v2")
public class UserTaskController {

    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) throws UserAlreadyExistException, UserNotFoundException, ProjectMemberAlreadyExistException {
        try{
            User newUser=userService.saveUser(user);
            return new ResponseEntity<User>(newUser, HttpStatus.CREATED);

        } catch (UserAlreadyExistException exception) {
            throw new UserAlreadyExistException();
        }
    }
    @GetMapping("/secure/getUser/{emailId}")
    public ResponseEntity<?> getUser(@PathVariable String emailId) throws UserAlreadyExistException, UserNotFoundException {
        try{
            User user=userService.getUser(emailId);
            return new ResponseEntity<User>(user, HttpStatus.OK);
        } catch (UserNotFoundException exception) {
            throw new UserNotFoundException();
        }
    }

    @PostMapping("/secure/createProject/{emailId}")
    public ResponseEntity<?> createProject(@PathVariable String emailId, @RequestBody Project project) throws ProjectAlreadyExistException, UserNotFoundException {
        System.out.println(project.getProjectName());
        try{

            User user=userService.createProject(emailId,project);
         return new ResponseEntity<Project>(project,HttpStatus.CREATED);
        }
        catch (UserNotFoundException exception)
        {
            throw new UserNotFoundException();
        }
        catch (ProjectAlreadyExistException exception)
        {
            throw new  ProjectAlreadyExistException();
        }
        catch(Exception e)
        {
            e.printStackTrace();
           return new ResponseEntity<>("Error!!! Try after sometime",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/secure/projects/{emailId}")
    public ResponseEntity<?> getAllProjects(@PathVariable String emailId) throws  UserNotFoundException
    {
        try{
            List<Project> projectList=userService.getAllProjects(emailId);
           return new ResponseEntity<>(projectList,HttpStatus.OK);
        }
        catch (UserNotFoundException exception)
        {
            throw new UserNotFoundException();
        }
        catch(Exception e)
        {
            e.printStackTrace();
          return new ResponseEntity<>("Error!!! Try after sometime",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @DeleteMapping("/secure/deleteProject/{emailId}/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable String emailId,@PathVariable int  projectId) throws UserNotFoundException {
        try
        {
            User user=userService.deleteProject(emailId,projectId);
        return new ResponseEntity(user,HttpStatus.OK);
        }
        catch (UserNotFoundException exception)
        {
            throw new UserNotFoundException();
        }
        catch(Exception e)
        {
            e.printStackTrace();
           return new ResponseEntity<String>("Error!!! Try after sometime",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/secure/project/{emailId}/{projectId}")
    public ResponseEntity<?> getProject(@PathVariable String emailId,@PathVariable int projectId) throws UserNotFoundException {
        try{
            Project project=userService.getProject(emailId,projectId);
            return new ResponseEntity<>(project,HttpStatus.OK);
        }
        catch (UserNotFoundException exception)
        {
            throw new UserNotFoundException();
        }

        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<String>("Error!!! Try after sometime",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PostMapping("/secure/addProjectMember/{emailId}")
    public ResponseEntity<?> addProjectMember(@PathVariable String emailId, @RequestBody ProjectMember projectMember) throws UserNotFoundException, ProjectMemberAlreadyExistException {
        try{
            User user=userService.addProjectMember(emailId,projectMember);
            return new ResponseEntity<>(user,HttpStatus.CREATED);
        }
        catch (UserNotFoundException exception)
        {
            throw new UserNotFoundException();
        }
        catch (ProjectMemberAlreadyExistException exception)
        {
            throw new ProjectMemberAlreadyExistException();
        }
        catch(Exception e)
        {
            e.printStackTrace();
           return new ResponseEntity<String>("Error!!! Try after sometime",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/secure/projectMembers/{emailId}")
    public ResponseEntity<?>  getAllProjectMembers(@PathVariable String emailId) throws UserNotFoundException {
        try{
            List<ProjectMember> projectMemberList=userService.getAllProjectMembers(emailId);
            return new ResponseEntity<List<ProjectMember>>(projectMemberList,HttpStatus.OK);
        }
        catch (UserNotFoundException exception)
        {
            throw new UserNotFoundException();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity<String>("Error!!! Try after sometime",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/secure/deleteProjectMember/{emailId}/{memberId}")
    public ResponseEntity<?> deleteProjectMember(@PathVariable String emailId,@PathVariable int memberId) throws UserNotFoundException {
        System.out.println("deleting member");
        try
        {
            User user=userService.deleteProjectMember(emailId,memberId);
            System.out.println(user);
            return new ResponseEntity<>(user,HttpStatus.OK);

        }
        catch (UserNotFoundException exception)
        {
            throw new UserNotFoundException();
        }
              catch(Exception e)
        {
            e.printStackTrace();
         return new ResponseEntity("Error!!! Try after sometime",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/secure/createTask/{emailId}/{projectName}")
    public ResponseEntity<?> createTask(@PathVariable String emailId,@PathVariable String projectName,@RequestBody Task task) throws  UserNotFoundException, ProjectNotFoundException
    {
        try{

            User user=userService.createTask(emailId,projectName,task);
           return new ResponseEntity<>(task,HttpStatus.CREATED);
        }
        catch (UserNotFoundException exception)
        {
            throw new UserNotFoundException();
        }
        catch (ProjectNotFoundException exception)
        {
            throw new ProjectNotFoundException();
        } catch(Exception e)
        {
            e.printStackTrace();
           return new ResponseEntity("Error!!! Try after sometime",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/secure/getTasks/{emailId}/{projectName}")
    public ResponseEntity<?> getAllTask(@PathVariable String emailId,@PathVariable String projectName) throws  UserNotFoundException, ProjectNotFoundException
    {
        try{
            List<Task> taskList=userService.getAllTask(emailId,projectName);
            return new ResponseEntity(taskList,HttpStatus.OK);
        }
        catch (UserNotFoundException exception)
        {
            throw new UserNotFoundException();
        }
        catch (ProjectNotFoundException exception)
        {
            throw new ProjectNotFoundException();
        }

        catch(Exception e)
        {
            e.printStackTrace();
           return new ResponseEntity("Error!!! Try after sometime",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @GetMapping("/secure/getTaskById/{emailId}/{projectName}/{taskId}")
    public ResponseEntity getTaskById(@PathVariable String emailId,@PathVariable String projectName,@PathVariable int taskId) throws  UserNotFoundException, ProjectNotFoundException,TaskNotFoundException
    {
        try{
            Task task=userService.getTask(emailId,projectName,taskId);
            return new ResponseEntity(task,HttpStatus.OK);
        }
        catch (UserNotFoundException exception)
        {
            throw new UserNotFoundException();
        }
        catch (ProjectNotFoundException exception)
        {
            throw new ProjectNotFoundException();
        }
               catch (TaskNotFoundException exception)
        {
            throw new TaskNotFoundException();
        }
        catch(Exception e)
        {
            e.printStackTrace();
   return new ResponseEntity("Error!!! Try after sometime",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @PutMapping("/secure/updateTask/{emailId}/{projectName}/{taskId}")
    public ResponseEntity updateTask(@PathVariable String emailId,@PathVariable String projectName,@PathVariable int taskId,@RequestBody Task task) throws  UserNotFoundException, ProjectNotFoundException,TaskNotFoundException
    {
        try{
            User user=userService.updateTask(emailId,projectName,task,taskId);
            return new ResponseEntity(user,HttpStatus.OK);
        }
        catch (UserNotFoundException e)
        {
            throw new UserNotFoundException();
        }
                catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error!!! Try after sometime",HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    @DeleteMapping("/secure/deleteTask/{emailId}/{projectName}/{taskId}")
    public ResponseEntity deleteTask(@PathVariable String emailId,@PathVariable String projectName,@PathVariable int taskId) throws  UserNotFoundException, ProjectNotFoundException,TaskNotFoundException
    {
        try{
            User user=userService.deleteTask(emailId,projectName,taskId);
         return new ResponseEntity(user,HttpStatus.OK);
        }
        catch (UserNotFoundException e)
        {
            throw new UserNotFoundException();
        }
        catch (ProjectNotFoundException e)
        {
            throw new ProjectNotFoundException();
        }
        catch (TaskNotFoundException e)
        {
            throw new TaskNotFoundException();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return new ResponseEntity("Error!!! Try after sometime",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
