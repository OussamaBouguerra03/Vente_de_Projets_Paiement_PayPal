import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProjectService } from '../../services/project.service';
import { AuthService } from '../../services/auth.service';  
import { PayPalService } from '../../services/paypal.service';

@Component({
  selector: 'app-paymentsuccess',
  templateUrl: './paymentsuccess.component.html',
  styleUrls: ['./paymentsuccess.component.css']
})
export class PaymentsuccessComponent implements OnInit {
  paymentId!: string;
  payerId!: string;
  amount!: number;
  projectId!: number;

  constructor(
    private route: ActivatedRoute,
    private projectService: ProjectService,
    private authService: AuthService,  
    private payPalService: PayPalService  
  ) { }

  ngOnInit(): void {
     this.route.queryParams.subscribe(params => {
      this.paymentId = params['paymentId'];
      this.payerId = params['PayerID'];
      this.amount = +params['amount'];
      this.projectId = +params['projectId'];

       const userId = this.authService.getUserId();

       if (!userId) {
        console.error('Utilisateur non authentifié');
        return;
      }

       this.payPalService.handlePaymentSuccess(this.paymentId, this.payerId, this.projectId, this.amount, userId).subscribe(
        response => {
          console.log('Achat enregistré avec succès:', response);
         },
        error => {
          console.error('Erreur lors de l\'enregistrement de l\'achat:', error);
         }
      );
    });
  }
}
