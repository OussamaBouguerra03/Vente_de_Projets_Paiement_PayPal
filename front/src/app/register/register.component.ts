import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {
  signupForm: FormGroup;
  selectedFile: File | null = null;
  avatarPreviewUrl: string | ArrayBuffer | null = null;

  constructor(
    private fb: FormBuilder,
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService
  ) {
    this.signupForm = this.fb.group({
      username: ['', Validators.required],
      email: ['', [Validators.required, Validators.email]],
      bio: ['', Validators.maxLength(250)],
      password: ['', [
        Validators.required,
        Validators.minLength(8),
        Validators.pattern('^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$')
      ]],
      role: ['', Validators.required]
    });
  }

  get password() {
    return this.signupForm.get('password');
  }

  get username() {
    return this.signupForm.get('username');
  }

  get email() {
    return this.signupForm.get('email');
  }

  onFileChange(event: any): void {
    if (event.target.files.length > 0) {
      const file = event.target.files[0] as File;
      this.selectedFile = file;

      // Preview the image
      const reader = new FileReader();
      reader.onload = (e: any) => {
        this.avatarPreviewUrl = e.target.result;
      };
      reader.readAsDataURL(file);
    }
  }

  goToSignin(): void {
    this.router.navigate(['/login']);
  }

  onSubmit(): void {
    if (!this.signupForm.valid) {
      this.toastr.error('Please fill out all required fields before submitting.', 'Error');
      return;
    }

    if (!this.selectedFile) {
      this.toastr.error('Please select a file before submitting.', 'Error');
      return;
    }

    const { username, email, bio, password, role } = this.signupForm.value;
    console.log('Form data:', { username, email, password, role });

    this.authService.register(username, email, password, this.selectedFile, role).subscribe(
      response => {
        console.log('API response:', response);
        this.toastr.success('Registration successful! You can now log in.', 'Success');
        this.router.navigate(['/login']);
      },
      (error: any) => {
        console.log('API error:', error);

        if (error.status === 409) {
          if (error.error && error.error.message) {
            if (error.error.message.includes('email')) {
              this.toastr.error('This email is already in use.', 'Error');
            } else if (error.error.message.includes('username')) {
              this.toastr.error('This username is already in use.', 'Error');
            } else {
              this.toastr.error('Conflict: Username or email is already in use.', 'Error');
            }
          } else {
            this.toastr.error('Conflict: Username or email is already in use.', 'Error');
          }
        } else {
          this.toastr.error('Registration error. Please try again.', 'Error');
        }
      }
    );
  }
}
