import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Task } from '../model/task';

@Injectable({
  providedIn: 'root'
})
export class TaskService {

  constructor(private http:HttpClient) { }

  createTask(emailId:any, projectName:any, data:any){
   
    return this.http.post(`http://localhost:9000/api/v2/secure/createTask/${emailId}/${projectName}`,data);
  }
  getAllTasks(emailId:any, projectName:any){
    return this.http.get<Task[]>(`http://localhost:9000/api/v2/secure/getTasks/${emailId}/${projectName}`);
  }
  getTaskById(emailId:any, projectName:any, taskId:any){
    return this.http.get<Task>(`http://localhost:9000/api/v2/secure/getTaskById/${emailId}/${projectName}/${taskId}`);
  }
  deleteTaskById(emailId:any, projectName:any, taskId:number){
    return this.http.delete(`http://localhost:9000/api/v2/secure/deleteTask/${emailId}/${projectName}/${taskId}`);
  }
  updateTask(emailId:any, projectName:any, taskId:any, data:any){
    return this.http.put(`http://localhost:9000/api/v2/secure/updateTask/${emailId}/${projectName}/${taskId}`, data);
  }
}
