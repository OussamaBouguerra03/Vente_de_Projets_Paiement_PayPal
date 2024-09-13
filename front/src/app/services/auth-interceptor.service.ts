import { Injectable } from '@angular/core';
import { HttpInterceptor, HttpRequest, HttpHandler } from '@angular/common/http';
import { AuthService } from './auth.service';
@Injectable({
  providedIn: 'root',
})
export class AuthInterceptorService implements HttpInterceptor {
  constructor(private authService: AuthService) {}

  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const token = this.authService.getToken();
    console.log('Token récupéré:', token); // Ajout de log pour débogage
  
    // Ne pas ajouter le token pour les endpoints de login, register, reset, request, et PayPal
    if (req.url.includes('/login') || req.url.includes('/register') || req.url.includes('/reset') || req.url.includes('/request') || req.url.includes('/paypal')|| req.url.includes('/purchases')) {
      return next.handle(req);
    }
  
    if (token) {
      const clonedReq = req.clone({
        headers: req.headers.set('Authorization', `Bearer ${token}`),
      });
      return next.handle(clonedReq);
    }
  
    return next.handle(req);
  }
}

