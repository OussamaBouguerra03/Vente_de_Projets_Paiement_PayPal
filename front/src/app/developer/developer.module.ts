import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DeveloperRoutingModule } from './developer-routing.module';
import { DeveloperComponent } from './developer.component';
import { ProjectsComponent } from './projects/projects.component';
import { TasksComponent } from './tasks/tasks.component';
import { ProjectCreateComponent } from './project-create/project-create.component';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { UpdateProjectComponent } from './update-project/update-project.component';
import { SalesComponent } from './sales/sales.component';


@NgModule({
  declarations: [
    DeveloperComponent,
    ProjectsComponent,
    TasksComponent,
    ProjectCreateComponent,
    UpdateProjectComponent,
    SalesComponent
  ],
  imports: [
    CommonModule,
    DeveloperRoutingModule,
    ReactiveFormsModule, // Ajouter ici
    HttpClientModule,
    FormsModule
  ]
})
export class DeveloperModule { }
