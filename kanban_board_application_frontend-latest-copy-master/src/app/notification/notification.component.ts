import { Component, OnInit } from '@angular/core';
import { Task } from '../model/task';
import { NotificationService } from '../services/notification.service';
import { UserService } from '../services/user.service';
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-notification',
  templateUrl: './notification.component.html',
  styleUrls: ['./notification.component.css']
})
export class NotificationComponent implements OnInit {

  emailId1:any;
  projectList:any[]=[];

  constructor(private notificationService : NotificationService){}
  
  ngOnInit(): void {
    if(sessionStorage.getItem('token')){
      const a:any= sessionStorage.getItem('token');
      this.emailId1=jwt_decode<any>(a)['emailId'];
     } 
    this.notificationService.getNotification(this.emailId1).subscribe((res:any)=>{
         console.warn("Result..",res);
         this.projectList=res.projectLists;
         console.warn("Project List..",this.projectList);

    })
  }

  delete(projectName:any,data: any){
    this.notificationService.deleteNotification(projectName,data).subscribe((res)=>{
      alert('deleted');
      this.ngOnInit();
    })
   }
}
