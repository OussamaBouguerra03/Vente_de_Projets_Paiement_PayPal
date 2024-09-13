import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, tap, throwError } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private apiUrl = 'http://localhost:8980/api/auth';  
  private apiUrls = 'http://localhost:8980/api/user';  

  constructor(private http: HttpClient) {}

  refreshToken(token: string): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/refresh-token`, token);
  }
  login(credentials: any): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/login`, credentials).pipe(
      tap(response => {
        if (response && response.token) {
          this.storeToken(response.token);
           if (response.user) {
            this.setCurrentUser(response.user);
            console.log('Utilisateur connecté:', response.user);
          }
        }
      }),
      catchError(error => {
        console.error('Login error:', error);
        return throwError(() => new Error('La connexion a échoué. Veuillez réessayer.'));
      })
    );
  }

  storeToken(token: string): void {
    localStorage.setItem('authToken', token);
  }

  storeUserRole(role: string): void {
    localStorage.setItem('userRole', role);
  }

  storeUserId(userId: number): void {
    localStorage.setItem('userId', userId.toString());
  }

  getToken(): string | null {
    return localStorage.getItem('authToken');
  }

  getUserRole(): string | null {
    return localStorage.getItem('userRole');
  }

  getUserId(): number | null {
    const userId = localStorage.getItem('userId');
    return userId ? +userId : null;
  }

  logout(): void {
    localStorage.removeItem('authToken');
    localStorage.removeItem('userRole');
    localStorage.removeItem('userId');
  }

  setCurrentUser(user: any): void {
    this.storeUserId(user.id);
  }

  getCurrentUser(): any {
    const userId = this.getUserId();
    return { id: userId };
  }

  isAuthenticated(): boolean {
    return !!this.getToken();
  }

  register(username: string, email: string, password: string, avatarFile: File | null, role: string): Observable<any> {
    const formData = new FormData();
    formData.append('username', username);
    formData.append('email', email);
    formData.append('password', password);
    formData.append('role', role);
    if (avatarFile) {
      formData.append('picture', avatarFile, avatarFile.name);
    }

    return this.http.post(`${this.apiUrl}/register`, formData);
  }

  getUserInfo(): Observable<any> {
    return this.http.get<any>(`${this.apiUrls}/info`);
  }

  getProfilePicture(userId: number): Observable<Blob> {
    const url = `${this.apiUrls}/profile-picture/${userId}`;
    return this.http.get(url, { responseType: 'blob' }).pipe(
      catchError(error => {
        console.error('Error fetching profile picture:', error);
        return throwError(() => new Error('Unable to fetch profile picture'));
      })
    );
  }

  resetPassword(token: string, newPassword: string, confirmPassword: string): Observable<any> {
    const payload = {
      password: newPassword,
      confirmPassword: confirmPassword
    };

    return this.http.post<any>(`${this.apiUrl}/reset?token=${token}`, payload, {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    });
  }

  requestPasswordReset(requestDTO: any): Observable<any> {
    return this.http.post(`${this.apiUrl}/request`, requestDTO).pipe(
      catchError(error => {
        console.error('Password reset request error:', error);
        return throwError(() => new Error('Failed to request password reset. Please try again.'));
      })
    );
  }
}
