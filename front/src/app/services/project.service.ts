import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, throwError } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class ProjectService {
  private apiUrl = 'http://localhost:8980/api/projects';
  private apiUrls = 'http://localhost:8980/api/purchases';

  constructor(private http: HttpClient, private authService: AuthService) {}

  // Créer un projet
 
  createProject(projectData: any, videoFile: File): Observable<any> {
    const formData: FormData = new FormData();

    // Ajouter les données du projet en JSON dans le FormData
    formData.append('project', new Blob([JSON.stringify(projectData)], { type: 'application/json' }));

    // Ajouter la vidéo dans le FormData
    formData.append('video', videoFile);

    // Envoyer la requête HTTP POST
    return this.http.post(`${this.apiUrl}/create`, formData);
  }
  // Obtenir tous les projets pour l'utilisateur connecté
  getProjectsForCurrentUser(): Observable<any[]> {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && currentUser.id) {
      return this.http.get<any[]>(`${this.apiUrl}/user/${currentUser.id}`);
    } else {
      return new Observable(observer => {
        observer.error('Utilisateur non authentifié');
      });
    }
  }

  // Obtenir un projet par ID
  getProjectById(id: number): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/${id}`);
  }
 
  private static httpOptions = {
    headers: new HttpHeaders({
      'Content-Type': 'application/json'
    })
  };
  
 
  getProjectNameById(id: number): Observable<string> {
    const httpOptions = {
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    };
  
    return this.http.get<string>(`${this.apiUrl}/${id}/name`, httpOptions);
  }
  
  // Obtenir tous les projets
  getAllProjects(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  } 
  getProjects(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/all`);
  }

  // Mettre à jour un projet
  updateProject(id: number, formData: FormData): Observable<any> {
    return this.http.put<any>(`${this.apiUrl}/${id}`, formData);
  }

  // Supprimer un projet
  deleteProject(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  // Obtenir la vidéo d'un projet par ID
  getProjectVideo(id: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/video/${id}`, { responseType: 'blob' });
  }

  purchaseProject(projectId: number, amount: number, paymentId: string, userId: number): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/purchase`, {
      projectId,
      amount,
      paymentId,
      userId
    });
  }
  getUserPurchases(): Observable<any> {
    const token = this.authService.getToken();  // Récupère le token depuis le service AuthService
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`);  // Ajoute le token à l'en-tête
  
    return this.http.get<any[]>(`${this.apiUrls}/user`, { headers });
  }
  
  getRevenuesByDeveloper(userId: number): Observable<any[]> {
    const token = this.authService.getToken();  // Récupère le token depuis le service AuthService
    const headers = new HttpHeaders().set('Authorization', `Bearer ${token}`); 
    return this.http.get<any[]>(`${this.apiUrls}/developer/${userId}`, { headers });
  }
   
}
