import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { Member } from '../model/member';
import { MemberService } from '../services/member.service';
import { UserService } from '../services/user.service';
import jwt_decode from 'jwt-decode';

@Component({
  selector: 'app-add-member',
  templateUrl: './add-member.component.html',
  styleUrls: ['./add-member.component.css']
})
export class AddMemberComponent implements OnInit {


  memberForm: FormGroup;
  memberData: Member[] = [];

  emailId1:any;

  displayedColumns: string[] = ['memberId', 'memberName', 'memberEmailId', 'noOfTask', 'action'];

  constructor(private formbuider:FormBuilder, private memberService:MemberService, private  userService: UserService){
    this.memberForm = this.formbuider.group({
      memberName: new FormControl('',[Validators.required]),
      memberEmailId: new FormControl('',[Validators.required]),
      noOfTask: new FormControl(''),

    });
  }
  ngOnInit(): void {
    if(sessionStorage.getItem('token')){
      const a:any= sessionStorage.getItem('token');
      this.emailId1=jwt_decode<any>(a)['emailId'];
      this.memberService.getAllMembers(this.emailId1).subscribe(res=>{    
        this.memberData =res;
        console.log(res);
      })
    }
  }

  addMember(){
    console.log('member values 1: ' + this.memberForm.value)
    this.memberService.addMember(this.emailId1, this.memberForm.value).subscribe(res=>{
      this.memberForm.reset();
      console.log('member values: ' + this.memberForm.value);
      console.log(res);
      this.ngOnInit();
    })
  }

  deleteMember(id:any){
    this.memberService.deleteMember(this.emailId1, id).subscribe(res=>{
      // alert("Member deleted")
      console.log(res);
      this.ngOnInit();
    })
  }
}
