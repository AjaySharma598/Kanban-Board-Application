import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AddMemberComponent } from './add-member/add-member.component';
import { AddProjectComponent } from './add-project/add-project.component';
import { HomeComponent } from './home/home.component';
import { KanbanDashboardComponent } from './kanban-dashboard/kanban-dashboard.component';
import { LoginComponent } from './login/login.component';
import { NotificationComponent } from './notification/notification.component';
import { AuthGuard } from './services/auth.guard';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';

const routes: Routes = [
  {path:"", redirectTo: "/home", pathMatch: "full"},
  {path:'home',component: HomeComponent},
  {path:'login',component: LoginComponent},
  {path:'kanban-dashboard/:id', component: KanbanDashboardComponent, canActivate:[AuthGuard]},
  {path:'add-project', component: AddProjectComponent, canActivate:[AuthGuard]},
  {path:'add-member', component: AddMemberComponent, canActivate:[AuthGuard]},
  {path:'notification', component: NotificationComponent, canActivate:[AuthGuard]},
  {path: "**", component: PageNotFoundComponent},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
