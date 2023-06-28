import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Member } from '../model/member';

@Injectable({
  providedIn: 'root'
})
export class MemberService {

  constructor(private http:HttpClient) { }

  addMember(emailId:any, projectMember:any){
    return this.http.post(`http://localhost:9000/api/v2/secure/addProjectMember/${emailId}`,projectMember);
  }
  getAllMembers(emailId:any){
    return this.http.get<Member[]>(`http://localhost:9000/api/v2/secure/projectMembers/${emailId}`);
  }
  deleteMember(emailId:any,memberId:number){
    return this.http.delete(`http://localhost:9000/api/v2/secure/deleteProjectMember/${emailId}/${memberId}`);
  }
}
