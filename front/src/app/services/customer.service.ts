import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
 
@Injectable({
  providedIn: 'root'
})
export class CustomerService {

  private apiUrl = 'http://localhost:8980/api/customers';  

  constructor(private http: HttpClient) { }

  getProjects(): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/projects`);
  }
  getProjectVideo(projectId: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/video/${projectId}`, { responseType: 'blob' });
  }
  getUserImage(userId: number): Observable<Blob> {
    return this.http.get(`${this.apiUrl}/${userId}/image`, { responseType: 'blob' });
  }
}
