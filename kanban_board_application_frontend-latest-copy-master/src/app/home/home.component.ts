import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
 
  showLogin: any;
  emailId: any;
  username:any

  constructor(private userService:UserService, private router:Router){ }

  ngOnInit(): void {
    this.emailId = sessionStorage.getItem('token');
    this.router.events.subscribe(res=>{
      if(sessionStorage.getItem('token') || sessionStorage.getItem('userName')){
        this.username=sessionStorage.getItem('userName');
        this.showLogin=true; 
      }
      else{
        this.showLogin=false;
      }
    })
  }

}
