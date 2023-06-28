import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Project } from '../model/project';
import { ProjectService } from '../services/project.service';
import { UserService } from '../services/user.service';
import { ToastrService } from 'ngx-toastr';
import { NotificationService } from '../services/notification.service';
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-add-project',
  templateUrl: './add-project.component.html',
  styleUrls: ['./add-project.component.css']
})
export class AddProjectComponent implements OnInit {

projectData: Project[] =[];
projectForm: FormGroup;

email:any;
emailId1:any;

displayedColumns: string[] = ['projectId', 'projectName', 'action'];

  constructor(private formbuider:FormBuilder, private projectService:ProjectService, private userService:UserService,private notification:NotificationService,private  toastr :ToastrService){
    this.projectForm = this.formbuider.group({
      projectName:new FormControl('',[Validators.required]),
    });
  }
  ngOnInit(): void {
    if(sessionStorage.getItem('token')){
      const a:any= sessionStorage.getItem('token');
      this.emailId1=jwt_decode<any>(a)['emailId'];
      this.show();
       this.projectService.getAllProjects(this.emailId1).subscribe(res=>{
         this.projectData =res;
       })
     }
    
  }

  createProject(){
    this.projectService.addProject(this.emailId1, this.projectForm.value).subscribe(res=>{
      this.ngOnInit();
    })
  }

  deleteProject(id:any){
    this.projectService.deleteProject(this.emailId1, id).subscribe((res)=>{
      this.ngOnInit();
    })
  }

  show(){
    this.notification.getNotification(this.emailId1).subscribe((res:any)=>{
      let reminderMsg = "";
      for(let d of res.projectLists){
        reminderMsg='Remainder:-----------------------------'; 
        
        for(let c of d.taskList){
          if(c.priority=="High"){
          reminderMsg += `${d.projectName}  :  ${c.taskName}  :  ${c.priority}  :  ${c.memberName} <br>`;
        }
      }
      }
      this.toastr.error(reminderMsg, "", {
        enableHtml: true,
         positionClass: 'toast-top-right',
         toastClass:'ngx-toastr',
        timeOut: 6000
      });
    });
  }
}

