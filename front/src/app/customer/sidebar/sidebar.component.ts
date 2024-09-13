import { Component } from '@angular/core';
import { AuthService } from '../../services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css'
})
export class SidebarComponent {
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
     this.router.navigate(['/login']);
  }
}
