import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { ActivatedRoute, Router } from '@angular/router';
import { ToastrService } from 'ngx-toastr'; // Importer ToastrService

@Component({
  selector: 'app-new-password',
  templateUrl: './new-password.component.html',
  styleUrls: ['./new-password.component.css']
})
export class NewPasswordComponent implements OnInit {
  newPassword: string = '';
  confirmPassword: string = '';
  token: string = ''; // Token récupéré des paramètres de l'URL
  showPassword: boolean = false;  // Déclare la variable ici

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService // Injecter ToastrService
  ) {}
  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;  // Bascule entre 'text' et 'password'
  }

  ngOnInit() {
    // Récupérer le token de l'URL
    this.route.queryParams.subscribe(params => {
      this.token = params['token'];
    });
  }

  resetPassword() {
    console.log('New Password:', this.newPassword);
    console.log('Confirm Password:', this.confirmPassword);
  
    if (this.newPassword !== this.confirmPassword) {
      this.toastr.error('Les mots de passe ne correspondent pas.', 'Erreur');
      return;
    }
  
    this.authService.resetPassword(this.token, this.newPassword, this.confirmPassword).subscribe(
      response => {
        // Traitement en cas de succès
        this.toastr.success('Mot de passe réinitialisé avec succès!', 'Succès');
        this.router.navigate(['/login']);
      },
      error => {
        // Traitement en cas d'erreur
        this.toastr.success('Mot de passe réinitialisé avec succès!', 'Succès');
        this.router.navigate(['/login']);      }
    );
  }
}
