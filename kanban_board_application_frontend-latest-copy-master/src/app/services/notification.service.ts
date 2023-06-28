import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  emailId1:any;

  constructor(private http:HttpClient) { }

  getNotification(emailId:string){
    return this.http.get(`http://localhost:9000/api/v3/notifications/${emailId}`);
  }
  deleteNotification(projectName:any,data:any){
    this.emailId1 = sessionStorage.getItem("userEmailId");
   return this.http.delete(`http://localhost:9000/api/v3/notification/${this.emailId1}/${projectName}`,{body:data});
  }

}
