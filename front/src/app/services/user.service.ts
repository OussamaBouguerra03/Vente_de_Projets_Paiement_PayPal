// src/app/services/user.service.ts

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, Observable, switchMap, throwError } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://localhost:8980/api/user'; // URL de l'API

  constructor(private http: HttpClient,private authService: AuthService ) { }

  getUserInfo(): Observable<any> {
    const token = localStorage.getItem('authToken');

    if (!token) {
      // Si le token est null, lancez une erreur ou gérez le cas en conséquence
      return throwError(() => new Error('No token found'));
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.get<any>(`${this.apiUrl}/info`, { headers }).pipe(
      catchError(error => {
        if (error.status === 401) { // Token expiré
          return this.authService.refreshToken(token).pipe(
            switchMap(newToken => {
              localStorage.setItem('authToken', newToken);
              const newHeaders = new HttpHeaders({
                'Authorization': `Bearer ${newToken}`
              });
              return this.http.get<any>(`${this.apiUrl}/info`, { headers: newHeaders });
            }),
            catchError(refreshError => throwError(() => refreshError))
          );
        } else {
          return throwError(() => error);
        }
      })
    );
  }
  
  getUserProfilePicture(userId: number): Observable<any> {
    return this.http.get(`${this.apiUrl}/current-profile-picture/${userId}`, { responseType: 'text' });
  }
 
  
  updateUser(username: string, email: string, picture: File | null, token: string): Observable<any> {
    const formData = new FormData();
    formData.append('username', username);
    formData.append('email', email);
    if (picture) {
      formData.append('picture', picture);
    }

    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });

    return this.http.put<any>(`${this.apiUrl}/update`, formData, { headers }).pipe(
      catchError(error => {
        if (error.status === 401) { // Token expiré
          return this.refreshToken(token).pipe(
            switchMap(newToken => {
              localStorage.setItem('authToken', newToken);
              return this.updateUser(username, email, picture, newToken); // Réessayer la mise à jour avec le nouveau token
            }),
            catchError(refreshError => throwError(() => refreshError))
          );
        } else {
          return throwError(() => error);
        }
      })
    );
  }

 
 

  refreshToken(oldToken: string): Observable<string> {
    return this.http.post<any>('http://localhost:8980/api/auth/refresh', { token: oldToken }).pipe(
      map((response: any) => response.token), // Convertir la réponse en token string
      catchError(error => throwError(() => error)) // Gérer les erreurs
    );
  }
}
