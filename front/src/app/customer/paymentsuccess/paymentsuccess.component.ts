import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProjectService } from '../../services/project.service';
import { AuthService } from '../../services/auth.service'; // Importer le service d'authentification
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
    private authService: AuthService, // Injecter le service d'authentification
    private payPalService: PayPalService // Injecter le service PayPal
  ) { }

  ngOnInit(): void {
    // Récupère les paramètres de la requête (paymentId, PayerID, amount, projectId)
    this.route.queryParams.subscribe(params => {
      this.paymentId = params['paymentId'];
      this.payerId = params['PayerID'];
      this.amount = +params['amount'];
      this.projectId = +params['projectId'];

      // Récupère l'ID de l'utilisateur depuis le service d'authentification
      const userId = this.authService.getUserId();

      // Vérifier si l'utilisateur est authentifié avant de procéder
      if (!userId) {
        console.error('Utilisateur non authentifié');
        return;
      }

      // Enregistrer l'achat après le succès du paiement
      this.payPalService.handlePaymentSuccess(this.paymentId, this.payerId, this.projectId, this.amount, userId).subscribe(
        response => {
          console.log('Achat enregistré avec succès:', response);
          // Ajouter une redirection ou un message de confirmation ici
        },
        error => {
          console.error('Erreur lors de l\'enregistrement de l\'achat:', error);
          // Ajouter une gestion d'erreur ici (message à l'utilisateur ou autre)
        }
      );
    });
  }
}
