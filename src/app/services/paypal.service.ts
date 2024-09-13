import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class PayPalService {

  private baseUrl = 'http://localhost:8980/api/paypal';  

  constructor(private http: HttpClient,    private authService: AuthService  
  ) { }

  createPayment(amount: number, projectId: number): void {
    const token = this.authService.getToken();  
  
    const headers = new HttpHeaders({
      'Authorization': `Bearer ${token}`
    });
  
    this.http.get<string>(`${this.baseUrl}/pay?amount=${amount}&projectId=${projectId}`, {
      responseType: 'text' as 'json',
      headers: headers
    }).subscribe(
      (approvalUrl: string) => {
        window.location.href = approvalUrl;  
      },
      (error) => {
        console.error('Error creating payment', error);
      }
    );
  }
  
  handlePaymentSuccess(paymentId: string, payerId: string, projectId: number, amount: number, userId: number): Observable<any> {
    return this.http.get<any>(`${this.baseUrl}/success?paymentId=${paymentId}&PayerID=${payerId}&projectId=${projectId}&amount=${amount}&userId=${userId}`);
  }
  
}
