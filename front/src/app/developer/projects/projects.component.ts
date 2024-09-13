import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../services/project.service';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-projects',
  templateUrl: './projects.component.html',
  styleUrls: ['./projects.component.css']
})
export class ProjectsComponent implements OnInit {
  projects: any[] = []; // Utilisation de 'any' pour éviter les modèles

  constructor(
    private projectService: ProjectService,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService // Ajoutez ToastrService
  ) { }

  ngOnInit(): void {
    if (this.authService.isAuthenticated()) {
      console.log('Utilisateur authentifié:', this.authService.getCurrentUser()); // Ajoutez ce log
      this.loadProjects();
    } else {
      console.error('Utilisateur non authentifié');
      this.toastr.error('You need to be logged in to view projects.', 'Authentication Required'); // Affiche un toast d’erreur
      this.router.navigate(['/login']); // Redirection vers la page de connexion si nécessaire
    }
  }

  loadProjects(): void {
    this.projectService.getProjectsForCurrentUser().subscribe(
      (data: any[]) => {
        this.projects = data;
        this.toastr.success('Projects loaded successfully', 'Success'); // Affiche un toast de succès
        this.loadProjectVideos();
      },
      error => {
        console.error('Erreur lors de la récupération des projets', error);
        this.toastr.error('Failed to load projects', 'Error'); // Affiche un toast d’erreur
      }
    );
  }

  loadProjectVideos(): void {
    this.projects.forEach(project => {
      this.projectService.getProjectVideo(project.id).subscribe(
        videoBlob => {
          const videoUrl = URL.createObjectURL(videoBlob);
          project.videoUrl = videoUrl; // Ajoutez l'URL vidéo au projet
        },
        error => {
          console.error(`Erreur lors de la récupération de la vidéo du projet ${project.id}`, error);
        }
      );
    });
  }
  deleteProject(id: number): void {
    Swal.fire({
      title: 'Are you sure?',
      text: 'You won\'t be able to revert this!',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#3085d6',
      cancelButtonColor: '#d33',
      confirmButtonText: 'Yes, delete it!',
      cancelButtonText: 'Cancel'
    }).then((result) => {
      if (result.isConfirmed) {
        this.projectService.deleteProject(id).subscribe(
          () => {
            this.projects = this.projects.filter(project => project.id !== id);
            this.toastr.success('Project deleted successfully', 'Success'); // Affiche un toast de succès
            Swal.fire(
              'Deleted!',
              'Your project has been deleted.',
              'success'
            );
          },
          error => {
            console.error('Error while deleting the project', error);
            this.toastr.error('Failed to delete project', 'Error'); // Affiche un toast d’erreur
          }
        );
      }
    });
  }

  updateProject(id: number): void {
    this.router.navigate([`/developer/update/${id}`]); // Assurez-vous que ce chemin est configuré dans votre routeur
  }

  navigateToCreateProject(): void {
    this.router.navigate(['/developer/create']); // Assurez-vous que ce chemin est configuré dans votre routeur
  }
}
