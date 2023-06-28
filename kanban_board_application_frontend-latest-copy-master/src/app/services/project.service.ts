import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Project } from '../model/project';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {

  project:any;

  constructor(private http:HttpClient) { }

  addProject(emailId:any,projectdata:any){
   
    return this.http.post(`http://localhost:9000/api/v2/secure/createProject/${emailId}`,projectdata);
  }
  getAllProjects(emailId:any){
    return this.http.get<Project[]>(`http://localhost:9000/api/v2/secure/projects/${emailId}`); 
  }
  deleteProject(emailId:any,projectId:number){
    return this.http.delete(`http://localhost:9000/api/v2/secure/deleteProject/${emailId}/${projectId}`);
  }
  getProjectById(emailId:any,projectId:any){
    return this.http.get(`http://localhost:9000/api/v2/secure/project/${emailId}/${projectId}`);
  }
  updateProject(projectdata:any, emailId:any, projectId:number){
    return this.http.put(`http://localhost:9000/api/v2/secure/updateProject/${emailId}/${projectId}`, projectdata);
  }
}
