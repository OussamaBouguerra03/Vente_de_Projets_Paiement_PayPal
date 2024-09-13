import { Component, OnInit } from '@angular/core';
import { ProjectService } from '../../services/project.service';

@Component({
  selector: 'app-purchase',
  templateUrl: './purchase.component.html',
  styleUrl: './purchase.component.css'
})
export class PurchaseComponent implements OnInit{
  purchases: any[] = [];
  constructor(private purchaseService: ProjectService) { }

  ngOnInit(): void {
    this.purchaseService.getUserPurchases().subscribe((data: any[]) => {
      console.log(data);  // Pour voir les données dans la console
      this.purchases = data;
    });
  }
}
