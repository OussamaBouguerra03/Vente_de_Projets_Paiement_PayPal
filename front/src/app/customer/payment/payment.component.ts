import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { PayPalService } from '../../services/paypal.service';

@Component({
  selector: 'app-payment',
  templateUrl: './payment.component.html',
  styleUrls: ['./payment.component.css']
})
export class PaymentComponent implements OnInit {
  amount!: number;
  projectId!: number;

  constructor(
    private payPalService: PayPalService,
    private route: ActivatedRoute
  ) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      this.amount = +params['amount'];
      this.projectId = +params['projectId'];
    });
  }

  onPay(): void {
    if (this.amount && this.projectId) {
       this.payPalService.createPayment(this.amount, this.projectId);
    } else {
      console.error('Amount and Project ID are required for payment');
    }
  }
  
}