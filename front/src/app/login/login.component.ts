import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  username: string = '';
  password: string = '';
  loginError: boolean = false;  // Flag to show error alert
  loginErrorMessage: string = '';  // Error message to display
  passwordVisible: boolean = false; // Flag to show/hide password

  credentials = {
    username: '',
    password: '',
  };

  constructor(private authService: AuthService, private router: Router) {}

  login() {
    this.credentials.username = this.username;
    this.credentials.password = this.password;

    this.authService.login(this.credentials).subscribe(
      (response: any) => {
        // Reset errors after success
        this.loginError = false;

        if (response.user && response.user.id) {
          this.authService.storeToken(response.token);
          this.authService.storeUserRole(response.role);
          this.authService.storeUserId(response.user.id);

          if (response.role === 'ADMIN') {
            this.router.navigate(['/admin']);
          } else if (response.role === 'DEVELOPPER') {
            this.router.navigate(['/developer/list']);
          } else if (response.role === 'CUSTOMER') {
            this.router.navigate(['/customer/home']);
          } else {
            this.loginError = true;
            this.loginErrorMessage = 'Unknown role. Please contact the administrator.';
          }
        } else {
          this.loginError = true;
          this.loginErrorMessage = 'User information is missing in the response.';
        }
      },
      (error) => {
        console.error('Login failed', error);
        this.loginError = true;  // Show error
        this.loginErrorMessage = 'Login failed. Please check your credentials.';
      }
    );
  }

  navigateToRegister() {
    this.router.navigate(['/register']);
  }

  navigateToForgotPassword() {
    this.router.navigate(['/reset-password']);
  }

  togglePasswordVisibility() {
    this.passwordVisible = !this.passwordVisible;
  }
}
