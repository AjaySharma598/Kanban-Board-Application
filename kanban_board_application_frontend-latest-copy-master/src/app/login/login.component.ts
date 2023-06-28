import { Component, OnInit } from '@angular/core';
import { AbstractControl, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { UserService } from '../services/user.service';
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  
  showLogin:boolean=true;
  hide = true;
  emailId1:any;
  
// for registeration form

  registerForm:FormGroup;
  loginForm:FormGroup;
  myControl = new FormControl();

  constructor(private formbuider:FormBuilder,
    private service:UserService,private router:Router, 
    private userService:UserService, private _snackBar: MatSnackBar) {
    // for registeration form
    this.registerForm = this.formbuider.group({
          userName:new FormControl('',[Validators.required,Validators.minLength(3),Validators.pattern('[a-zA-Z][a-zA-Z]{1,}')]),
          emailId:new FormControl('',[Validators.required,Validators.pattern(/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/)]),
          password:new FormControl('',[Validators.required,Validators.pattern(/^(?=.*\d)(?=.*[!@#$%^&*])(?=.*[a-z])(?=.*[A-Z]).{4,}$/)]),
          confirmPassword:new FormControl('',[Validators.required]),
    },{validator:[this.passwordShouldMatchValidation]});

    // for login form
    this.loginForm = this.formbuider.group({
      emailId:new FormControl('',[Validators.required]),
      password:new FormControl('',[Validators.required])
      
    });
  }

  ngOnInit(): void {}
  
get userName(){
  return this.registerForm.get('userName');
}
get emailId(){
  return this.registerForm.get('emailId');
}
get password(){
  return this.registerForm.get('password');
}
get confirmPassword(){
  return this.registerForm.get('confirmPassword');
}

passwordShouldMatchValidation(myControl:AbstractControl){
  const passwordValue=myControl.get('password')?.value;
  const confirmPasswordValue=myControl.get('confirmPassword')?.value;
  if(!passwordValue || !confirmPasswordValue){
    return null;
 }
if(passwordValue != confirmPasswordValue){
  return {passwordShouldMatch : false};   
}
return null;
}

register(){
  this.service.register(this.registerForm.value).subscribe((res)=>{
    this.ngOnInit();
    this._snackBar.open('Congrats, you have registered successfully!!', 'success', {​
      duration: 5000,
      panelClass: ['mat-toolbar', 'mat-primary']
    })
  });
    this.showLogin = true;
}


login(){
  this.service.login(this.loginForm.value).subscribe({
    next:(data:any)=>{
      sessionStorage.setItem("token",data.token);
      this.emailId1=jwt_decode<any>(data.token)['emailId'];
      this.userService.getUser(this.emailId1);
      this.service.isLoggedIn=true;
      this.ngOnInit();
      this._snackBar.open('Logged in successfully!!', 'success', {​
        duration: 5000,
        panelClass: ['mat-toolbar', 'mat-primary']
      })
      this.loginForm.reset();
      this.router.navigate(['/add-project']);
    },
    error: err=>{
      this._snackBar.open('Email or Password Does not Match!!!!', 'error', {​
        duration: 5000,
        panelClass: ['mat-toolbar', 'mat-primary']
      })
    }
  });
}
  showRegister(){
    this.showLogin=false;
  }
  openLogin(){
    this.showLogin=true;
  }
}
