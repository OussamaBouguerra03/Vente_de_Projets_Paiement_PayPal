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
  token: string = '';  
  showPassword: boolean = false;   

  constructor(
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private toastr: ToastrService  
  ) {}
  togglePasswordVisibility() {
    this.showPassword = !this.showPassword;   
  }

  ngOnInit() {
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
         this.toastr.success('Mot de passe réinitialisé avec succès!', 'Succès');
        this.router.navigate(['/login']);
      },
      error => {
         this.toastr.success('Mot de passe réinitialisé avec succès!', 'Succès');
        this.router.navigate(['/login']);      }
    );
  }
}
