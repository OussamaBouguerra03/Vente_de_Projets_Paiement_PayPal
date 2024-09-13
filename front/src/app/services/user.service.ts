// src/app/services/user.service.ts

import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, map, Observable, switchMap, throwError } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private apiUrl = 'http://localhost:8980/api/user';  

  constructor(private http: HttpClient,private authService: AuthService ) { }

  getUserInfo(): Observable<any> {
    const token = localStorage.getItem('authToken');

    if (!token) {
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
        if (error.status === 401) {  
          return this.refreshToken(token).pipe(
            switchMap(newToken => {
              localStorage.setItem('authToken', newToken);
              return this.updateUser(username, email, picture, newToken); 
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
      map((response: any) => response.token),  
      catchError(error => throwError(() => error)) 
    );
  }
}
