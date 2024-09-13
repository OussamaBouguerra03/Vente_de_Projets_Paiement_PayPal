import { Component } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr'; // Importer ToastrService

@Component({
  selector: 'app-reset-password',
  templateUrl: './reset-password.component.html',
  styleUrls: ['./reset-password.component.css']
})
export class ResetPasswordComponent {
  email: string = '';
  resetError: boolean = false;  // Flag pour afficher l'alerte d'erreur
  resetErrorMessage: string = '';  // Message d'erreur à afficher

  constructor(
    private authService: AuthService,
    private router: Router,
    private toastr: ToastrService // Injecter ToastrService
  ) {}

  requestPasswordReset() {
    this.authService.requestPasswordReset({ email: this.email }).subscribe(
      () => {
        // Réinitialisation des erreurs après un succès
        this.resetError = false;
        this.toastr.success('Password reset email sent. Please check your inbox.', 'Success'); // Utiliser toastr pour le succès
        this.router.navigate(['/login']);
      },
      (error) => {
        this.resetError = false;
        this.toastr.success('Password reset email sent. Please check your inbox.', 'Success'); // Utiliser toastr pour le succès
        this.router.navigate(['/login']); }
    );
  }

  navigateToLogin() {
    this.router.navigate(['/login']);
  }
}
