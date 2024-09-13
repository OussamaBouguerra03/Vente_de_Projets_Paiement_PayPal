import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { DeveloperComponent } from './developer.component';
import { ProjectCreateComponent } from './project-create/project-create.component';
import { DeveloperGuard } from '../guards/developer.guard';
import { ProjectsComponent } from './projects/projects.component';
import { UpdateProjectComponent } from './update-project/update-project.component';
import { SalesComponent } from './sales/sales.component';

const routes: Routes = [
  {
    path: '',
    component: DeveloperComponent,
    children: [
      { path: 'create', component: ProjectCreateComponent, canActivate: [DeveloperGuard] },
      { path: 'list', component: ProjectsComponent, canActivate: [DeveloperGuard] },
      { path: 'update/:id', component: UpdateProjectComponent },
      { path: 'sales', component: SalesComponent },
      { path: '', redirectTo: 'list', pathMatch: 'full' } // Optionnel : redirige vers 'list' par défaut
    ]
  },
  // Autres routes ici
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class DeveloperRoutingModule { }
