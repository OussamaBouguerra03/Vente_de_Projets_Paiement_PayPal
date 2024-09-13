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
     this.userId = this.authService.getUserId();

    this.projectForm = this.fb.group({
      name: ['', Validators.required],
      description: ['', Validators.required],
      price: ['', Validators.required],
      userId: [this.userId, Validators.required],  
      technologies: ['', Validators.required],  
      category: ['', Validators.required]
    });
  }

   onVideoSelected(event: any) {
    const file: File = event.target.files[0];
    if (file) {
      this.selectedVideo = file;
    }
  }

   
  onSubmit() {
    if (this.projectForm.valid && this.selectedVideo) {
      const projectData = this.projectForm.value;

       this.projectService.createProject(projectData, this.selectedVideo).subscribe({
        next: (response) => {
          this.toastr.success('Project created successfully!', 'Success');  
          console.log('Project created successfully', response);
          this.router.navigate(['/developer/list']);  
        },
        error: (err) => {
          this.toastr.error('Failed to create project', 'Error'); 
          console.error('Error creating project', err);
        }
      });
    } else {
      this.toastr.warning('Form is invalid or video file is missing', 'Warning');  
      console.log('Form is invalid or video file is missing');
    }
  }
}
