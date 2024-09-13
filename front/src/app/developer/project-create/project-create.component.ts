import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { ProjectService } from '../../services/project.service';
import { AuthService } from '../../services/auth.service';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-project-create',
  templateUrl: './project-create.component.html',
  styleUrls: ['./project-create.component.css']
})
export class ProjectCreateComponent {
  projectForm: FormGroup;
  selectedVideo: File | null = null;
  userId: number | null = null;

  constructor(private fb: FormBuilder, private projectService: ProjectService, private authService: AuthService,private toastr: ToastrService, // Injecter le service Toastr
    private router: Router) {
    // Récupérer l'ID de l'utilisateur connecté via AuthService
    this.userId = this.authService.getUserId();

    this.projectForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      price: ['', Validators.required],
      userId: [this.userId, Validators.required], // Utiliser l'ID utilisateur connecté
      technologies: ['', Validators.required], // Initialisez avec une chaîne de caractères vide
      category: ['', Validators.required]
    });
  }

  // Méthode pour gérer la sélection du fichier vidéo
  onVideoSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedVideo = file;
    }
  }

  // Méthode pour soumettre le formulaire
  
  onSubmit() {
    if (this.projectForm.valid && this.selectedVideo) {
      const projectData = this.projectForm.value;

      // Appel du service pour envoyer les données au backend
      this.projectService.createProject(projectData, this.selectedVideo).subscribe({
        next: (response) => {
          this.toastr.success('Project created successfully!', 'Success'); // Afficher un toast en cas de succès
          console.log('Project created successfully', response);
          this.router.navigate(['/developer/list']); // Rediriger après le succès
        },
        error: (err) => {
          this.toastr.error('Failed to create project', 'Error'); // Afficher un toast en cas d'erreur
          console.error('Error creating project', err);
        }
      });
    } else {
      this.toastr.warning('Form is invalid or video file is missing', 'Warning'); // Alerte si formulaire non valide
      console.log('Form is invalid or video file is missing');
    }
  }
}
