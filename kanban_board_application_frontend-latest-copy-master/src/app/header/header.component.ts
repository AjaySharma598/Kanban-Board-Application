import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../model/user';
import { UserService } from '../services/user.service';
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  emailId: any;
  loginStatus: boolean = true;
  username:any

  showLogin: any;


  constructor(private userService:UserService, private router:Router){ }

  ngOnInit(): void {
    
    this.router.events.subscribe(res=>{
      if(sessionStorage.getItem('token') || sessionStorage.getItem('userName')){
        this.username=sessionStorage.getItem('userName');
        this.showLogin=false; 
        console.warn('header testing' + this.username)
      }
      else{
        this.showLogin=true;
      }
    })
  }
  
  logOut(){
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('userName');
    this.userService.isLoggedIn=false;
  }

}
