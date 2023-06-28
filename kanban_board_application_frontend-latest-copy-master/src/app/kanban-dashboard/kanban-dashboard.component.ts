import { Component, Input, OnInit } from '@angular/core';
import { Task } from '../model/task';
import { UserService } from '../services/user.service';
import { CdkDragDrop, moveItemInArray, transferArrayItem} from '@angular/cdk/drag-drop';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { User } from '../model/user';
import { TaskService } from '../services/task.service';
import { ProjectService } from '../services/project.service';
import { ActivatedRoute } from '@angular/router';
import { Member } from '../model/member';
import { MemberService } from '../services/member.service';
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-kanban-dashboard',
  templateUrl: './kanban-dashboard.component.html',
  styleUrls: ['./kanban-dashboard.component.css']
})
export class KanbanDashboardComponent implements OnInit{

  taskForm: FormGroup;
  updateForm: FormGroup;
  taskId:any;
  projectName:any;
  userTask: Task = {};

  show:boolean=false;
  //this userData will contain user Data
  userData:User[]=[];
  memberData:Member[]=[];

  task:Task[]=[];
  size:any;
  // taskInProgress:Task[]=[];
  // taskCompleted:Task[]=[];

  emailId1:any;
  username:any;

  currentDate:Date;

  disableSelect = new FormControl(false);

  constructor(private service:UserService,private formbuider:FormBuilder, 
    private taskService:TaskService, private activatedRoute: ActivatedRoute,
    private userService:UserService, private memberService:MemberService){
    this.currentDate=new Date();
    this.taskForm = this.formbuider.group({
      taskName: new FormControl('',[Validators.required]),
      taskDescription: new FormControl('',[Validators.required]),
      priority: new FormControl('',[Validators.required]),
      taskStatus: new FormControl('',[Validators.required]),
      taskDate: new FormControl('',[Validators.required]),
      memberName: new FormControl('',[Validators.required]),
    });

    //for update form so that it can show value already added
    this.updateForm = this.formbuider.group({
      taskName:new FormControl('',[Validators.required]),
      priority:new FormControl('',[Validators.required]),
      taskDescription:new FormControl('',[Validators.required]),
      taskStatus: new FormControl('',[Validators.required]),
      taskDate: new FormControl('',[Validators.required]),
      memberName: new FormControl('',[Validators.required]),
    });

    this.username = this.taskForm.value.memberName;
  }

  ngOnInit(): void {
    if(sessionStorage.getItem('token')){
      const a:any= sessionStorage.getItem('token');
      this.emailId1=jwt_decode<any>(a)['emailId'];
     }
    this.activatedRoute.paramMap.subscribe(data => {
      let name = data.get('id') ?? 0;
      this.projectName = name;
      console.log('id in ngoninit: ' + name)
      this.taskService.getAllTasks(this.emailId1, name).subscribe((res:any)=>{
        console.warn("User Data....",res);
        this.task=res;
        console.log(this.task);
      })
    });

    this.memberService.getAllMembers(this.emailId1).subscribe(res=>{
      this.memberData=[];
      for(let resData of res) {
        if(resData.noOfTask<3)
        {
          this.memberData.push(resData);
        }
      }
     
    })
  }

  get taskName(){
    return this.taskForm.get('taskName');
  }
  get taskDescription(){
    return this.taskForm.get('taskDescription');
  }
  get priority(){
    return this.taskForm.get('priority');
  }
  get taskStatus(){
    return this.taskForm.get('taskStatus');
  }
  get taskDate(){
    return this.taskForm.get('taskDate');
  }
  get memberName(){
    return this.taskForm.get('memberName');
  }
  

  newTask(){
    console.warn(this.taskForm.value);
    this.taskService.createTask(this.emailId1, this.projectName ,this.taskForm.value).subscribe((res:any)=>{
      console.warn(res);
      this.ngOnInit();
    });
    this.show=false;
  }


  id1:any;
  updateTaskId(id:any){
  this.id1=id;
    this.taskService.getTaskById(this.emailId1, this.projectName, this.id1).subscribe((res:Task)=>{
      //to render the task details in input field to update
        this.updateForm = this.formbuider.group({
          taskName:new FormControl(res['taskName']),
          priority:new FormControl(res['priority']),
          taskDescription:new FormControl(res['taskDescription']),
          taskStatus: new FormControl(res['taskStatus']),
          taskDate: new FormControl(res['taskDate']),
          memberName: new FormControl(res['memberName']),
        });
        this.userTask=res;
      })
  }
  updateTask(){
    console.warn("updateTask Id",this.id1);
    this.taskService.updateTask(this.emailId1, this.projectName,this.id1,this.updateForm.value).subscribe((res)=>{
      console.warn("Task Updated ");
    
      this.ngOnInit();
    })
  }
  deleteTask(id:any){
  
    //here task id will be change
    this.taskService.deleteTaskById(this.emailId1, this.projectName, id).subscribe((res)=>{
      console.warn("deleted result",res);
      
      this.taskService.getAllTasks(this.emailId1, this.projectName).subscribe((res)=>{
        this.task=res;
        console.log(this.task);

        this.ngOnInit();
      })
    })
  }

  addTask(){
    this.show=true;
  }

  getInitials(username:any) {
    return username[0].toUpperCase();
  }
}
