import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { ProjectService } from '../../services/project.service';

@Component({
  selector: 'app-update-project',
  templateUrl: './update-project.component.html',
  styleUrls: ['./update-project.component.css']
})
export class UpdateProjectComponent implements OnInit {
  project: any = {
    name: '',
    description: '',
    price: 0
  };
  projectId!: number;

  constructor(
    private projectService: ProjectService,
    private route: ActivatedRoute,
    public router: Router   
  ) { }

  ngOnInit(): void {
    this.projectId = Number(this.route.snapshot.paramMap.get('id'));
    this.loadProject();
  }

  loadProject(): void {
    this.projectService.getProjectById(this.projectId).subscribe(
      (data) => {
        this.project = data;
      },
      (error) => {
        console.error('Erreur lors du chargement du projet', error);
        alert('Erreur lors du chargement du projet.');
      }
    );
  }

  updateProject(): void {
    this.projectService.updateProject(this.projectId, this.project).subscribe(
      () => {
        console.log('Projet mis à jour avec succès');
        this.router.navigate(['developer/list']);
      },
      (error) => {
        console.error('Erreur lors de la mise à jour du projet', error);
        alert('Erreur lors de la mise à jour du projet.');
      }
    );
  }
}
