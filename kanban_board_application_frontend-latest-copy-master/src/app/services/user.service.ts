import { HttpClient, HttpHeaders } from '@angular/common/http';
import { EventEmitter, Injectable, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../model/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  
  user: any;
  userEmailId:any;
  isLoggedIn: boolean = false;
  
  constructor(private http:HttpClient) { }

  login(data:any){
    return this.http.post("http://localhost:9000/api/v1/login",data);
  }
  register(data:any){
    return this.http.post("http://localhost:9000/api/v2/register",data);
  }
  getUser(emailId:string){
    return this.http.get(`http://localhost:9000/api/v2/secure/getUser/${emailId}`).subscribe((res:any)=>{
      console.log('user data: ' + res);
      sessionStorage.setItem("userName",res.userName);
    });
  }
}
