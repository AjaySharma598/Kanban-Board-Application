/*
 * Author : Anisha Palei
 * Date : 09-03-2023
 * Created with : IntelliJ IDEA Community Edition
 */

package com.niit.service;

import com.niit.domain.Project;
import com.niit.domain.ProjectMember;
import com.niit.domain.Task;
import com.niit.domain.User;
import com.niit.exception.*;
import com.niit.proxy.UserProxy;
import com.niit.rabbitMq.TaskDTO;
import com.niit.repository.TaskRepository;
import com.niit.repository.UserRepository;
import org.json.simple.JSONObject;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    UserRepository userRepository;

    @Autowired
    TaskRepository taskRepository;
    @Autowired
    UserProxy userProxy;

    @Autowired
    DirectExchange exchange;

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Override
    public User saveUser(User userDetail) throws UserAlreadyExistException, UserNotFoundException, ProjectMemberAlreadyExistException {

        if(userRepository.findById(userDetail.getEmailId()).isPresent()){
            throw new UserAlreadyExistException();
        }
        User newUser = userRepository.save(userDetail);
              if(!(newUser.getEmailId().isEmpty())){
            ResponseEntity responseEntity = userProxy.saveUserInAuth(newUser);
        }
        ProjectMember projectMember = new ProjectMember(userDetail.getUserName(), userDetail.getEmailId(), 0);
        this.addProjectMember(userDetail.getEmailId(),projectMember);
        return newUser;
    }
    @Override
    public User getUser(String emailId) throws UserNotFoundException {
        if (userRepository.findById(emailId).isEmpty()) {
            throw new UserNotFoundException();
        }
        User user = userRepository.findById(emailId).get();
        if (user!=null){
            return user;
        }
        return null;
    }

    @Override
    public User createProject(String emailId, Project project) throws UserNotFoundException, ProjectAlreadyExistException {

        if (userRepository.findById(emailId).isEmpty()) {

            throw new UserNotFoundException();

        }
        User user = userRepository.findById(emailId).get();
        List<Project> projectList;
        if(user.getProjectLists()==null)
        {
            project.setProjectId(1);
            projectList=new ArrayList<>();
        }
        else
        {
            projectList=user.getProjectLists();
            for(Project project1:projectList)
            {
                if(project1.getProjectName().equals(project.getProjectName()))
                {
                    throw new ProjectAlreadyExistException();
                }
            }
        }
        project.setProjectId(projectList.size()+1);
        projectList.add(project);
        user.setProjectLists(projectList);
        return userRepository.save(user);
    }

    @Override
    public List<Project> getAllProjects(String emailId) throws UserNotFoundException {
        if(userRepository.findById(emailId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        User user=userRepository.findById(emailId).get();
        return user.getProjectLists();
    }
    @Override
    public User deleteProject(String emailId, int projectId) throws UserNotFoundException {
        if(userRepository.findById(emailId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        System.out.println(projectId);
        User user=userRepository.findById(emailId).get();
        List<Project> projectList=user.getProjectLists();
        for(Project project1:projectList)
        {
            if(project1.getProjectId()== projectId)
            {
                projectList.remove(project1);
                break;
            }
        }

        user.setProjectLists(projectList);
        return userRepository.save(user);
    }

    @Override
    public Project getProject(String emailId, int projectId) throws UserNotFoundException {
        List<Project> projectList=this.getAllProjects(emailId);
        Project project=new Project();
        for(Project project1:projectList)
        {
            if(project1.getProjectId()==projectId)
            {
                project=project1;
            }
                   }
        return project;
    }

    @Override
    public User addProjectMember(String emailId, ProjectMember projectMember) throws UserNotFoundException, ProjectMemberAlreadyExistException {
        if(userRepository.findById(emailId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        User user=userRepository.findById(emailId).get();
        List<ProjectMember> projectMemberList=new ArrayList<>();
        if(user.getProjectMemberLists()==null)
        {
            projectMember.setMemberId(1);
             user.setProjectMemberLists(Arrays.asList(projectMember));
        }
        else
        {
            projectMemberList=user.getProjectMemberLists();
            for(ProjectMember projectMember1:projectMemberList)
            {
                if(projectMember1.getMemberEmailId().equals(projectMember.getMemberEmailId()))
                {
                    throw new ProjectMemberAlreadyExistException();
                }
            }
        }
        projectMember.setMemberId(projectMemberList.size()+1);
        projectMemberList.add(projectMember);
        return userRepository.save(user);
    }



    @Override
    public List<ProjectMember> getAllProjectMembers(String emailId) throws UserNotFoundException {
        if(userRepository.findById(emailId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        User user=userRepository.findById(emailId).get();
        List<ProjectMember> projectMemberList=user.getProjectMemberLists();
        return projectMemberList;
    }

    @Override
    public User deleteProjectMember(String emailId, int memberId) throws UserNotFoundException{
        if(userRepository.findById(emailId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        System.out.println(memberId);
        User user = userRepository.findById(emailId).get();
        List<ProjectMember> projectMemberList = user.getProjectMemberLists();
        for(ProjectMember projectMember1:projectMemberList)
        {
            if(projectMember1.getMemberId()== memberId)
            {
                projectMemberList.remove(projectMember1);
                break;
            }

        }
        user.setProjectMemberLists(projectMemberList);
        return userRepository.save(user);
    }

    @Override
    public User createTask(String emailId, String projectName, Task task) throws UserNotFoundException {
        if(userRepository.findById(emailId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        User user=userRepository.findById(emailId).get();
            Project project1=new Project();
        for(Project project:user.getProjectLists())
        {
            if(project.getProjectName().equals(projectName))
            {
                project1=project;
            }
                  }
        if (project1.getTaskList() == null) {
            task.setTaskId(1);
            project1.setTaskList(Arrays.asList(task));
        } else {

            List<Task> tasks = project1.getTaskList();
            task.setTaskId(tasks.size()+1);
            tasks.add(task);
            project1.setTaskList(tasks);
        }

        for (ProjectMember projectMember : user.getProjectMemberLists())
        {
            if (projectMember.getMemberName().equals(task.getMemberName()))
            {
                projectMember.setNoOfTask(projectMember.getNoOfTask() + 1);
            }
        }
        //to send the data from kanban service to Notification
        TaskDTO taskDTO=new TaskDTO();
        JSONObject jsonObject=new JSONObject();
        jsonObject.put("taskData",task);
        jsonObject.put("emailId",emailId);
        jsonObject.put("projectName",projectName);
        taskDTO.setJsonObject(jsonObject);
        rabbitTemplate.convertAndSend(exchange.getName(),"direct-routing",taskDTO);

        return userRepository.save(user);

    }

    @Override
    public List<Task> getAllTask(String emailId, String projectName) throws UserNotFoundException{
        if(userRepository.findById(emailId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        User user=userRepository.findById(emailId).get();
        Project project1=new Project();

        for(Project project:user.getProjectLists())
        {
            if(project.getProjectName().equals(projectName))
            {
                project1=project;
            }

        }

        List<Task> taskList=project1.getTaskList();
        return taskList;
    }

    @Override
    public Task getTask(String emailId, String projectName, int taskId) throws UserNotFoundException{
        List<Task> taskList=this.getAllTask(emailId,projectName);
        Task task=new Task();
        ;
        for (Task task1:taskList)
        {
            if(task1.getTaskId()==taskId)
            {
                task=task1;
                break;
            }
        }
              return task;
    }

    @Override
    public User updateTask(String emailId, String projectName, Task task,int taskId) throws UserNotFoundException {
        String currentMember=null;
        String previousMember=null;

        if(userRepository.findById(emailId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        User user=userRepository.findById(emailId).get();

        Project project1=new Project();
        for(Project project:user.getProjectLists())
        {
            if(project.getProjectName().equals(projectName))
            {
                project1=project;
                break;
            }
        }

        for(Task task1:project1.getTaskList())
        {
            if(task1.getTaskId()== taskId)
            {
                currentMember=task.getMemberName();
                previousMember=task1.getMemberName();
                task.setTaskId(task1.getTaskId());
                project1.getTaskList().set(project1.getTaskList().indexOf(task1),task);
                project1.setTaskList(project1.getTaskList());
            }
        }
        for (ProjectMember projectMember : user.getProjectMemberLists())
        {
            if (projectMember.getMemberName().equals(task.getMemberName()) && task.getTaskStatus().equalsIgnoreCase("Completed"))
            {
                projectMember.setNoOfTask(projectMember.getNoOfTask() - 1);
            }
            else if(projectMember.getMemberName().equalsIgnoreCase(currentMember)){
                projectMember.setNoOfTask(projectMember.getNoOfTask() + 1);

            }
            else if(projectMember.getMemberName().equalsIgnoreCase(previousMember)){
                projectMember.setNoOfTask(projectMember.getNoOfTask() - 1);
            }
        }
        return userRepository.save(user);
    }

    @Override
    public User deleteTask(String emailId, String projectName, int taskId) throws UserNotFoundException, ProjectNotFoundException, TaskNotFoundException {
        if(userRepository.findById(emailId).isEmpty())
        {
            throw new UserNotFoundException();
        }
        User user=userRepository.findById(emailId).get();
        Project project1=new Project();
        for(Project project:user.getProjectLists())
        {
            if(project.getProjectName().equals(projectName))
            {
                project1=project;
              break;
            }
        }

        for(Task task1:project1.getTaskList())
        {
            if(task1.getTaskId()==taskId)
            {
                    System.out.println(task1.getTaskId());
                    for(ProjectMember projectMember:user.getProjectMemberLists())
                   {
                    if(projectMember.getMemberName().equals(task1.getMemberName()))
                    {
                        projectMember.setNoOfTask(projectMember.getNoOfTask()-1);
                    }
                   }
                    project1.getTaskList().remove(task1);break;
                }
            }
        for (Task task2 : project1.getTaskList()) {
            task2.getTaskId();
        }
        project1.setTaskList(project1.getTaskList());
            return userRepository.save(user);
    }
}
