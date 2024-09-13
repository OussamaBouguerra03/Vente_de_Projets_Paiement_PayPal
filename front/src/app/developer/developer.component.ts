import { Component, OnInit } from '@angular/core';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-developer',
  templateUrl: './developer.component.html',
  styleUrls: ['./developer.component.css']
})
export class DeveloperComponent implements OnInit {
  user: any;
  profilePictureUrl: string | ArrayBuffer | null = '';

  constructor(private authService: AuthService, private router: Router) {}

  ngOnInit(): void {
    this.authService.getUserInfo().subscribe(data => {
      this.user = data;
      if (this.user && this.user.id) {
        this.authService.getProfilePicture(this.user.id).subscribe(blob => {
          const reader = new FileReader();
          reader.onloadend = () => {
            this.profilePictureUrl = reader.result;
          };
          reader.readAsDataURL(blob);
        });
      }
    });
  }
  logout(): void {
    this.authService.logout();
    this.router.navigate(['/login']); // Redirige vers la page de connexion
  }
}
