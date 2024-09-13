// developer.guard.ts
import { Injectable } from '@angular/core';
import { CanActivate, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
 
@Injectable({
  providedIn: 'root'
})
export class DeveloperGuard implements CanActivate {

   constructor(private authService: AuthService, private router: Router) {}

  canActivate(): boolean {
    const role = this.authService.getUserRole();
    if (role === 'DEVELOPPER') { // Assurez-vous que 'DEVELOPPER' est le rôle correct
      return true;
    } else {
      this.router.navigate(['/login']);
      return false;
    }
  }
}
