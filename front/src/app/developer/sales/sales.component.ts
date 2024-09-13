import { Component } from '@angular/core';
import { ProjectService } from '../../services/project.service';

@Component({
  selector: 'app-sales',
  templateUrl: './sales.component.html',
  styleUrl: './sales.component.css'
})
export class SalesComponent {
  purchases: any[] = [];
  totalRevenue: number = 0;
  projectNames: { [key: number]: string } = {}; // Pour stocker les noms des projets

  constructor(private purchaseService: ProjectService) {}

  ngOnInit(): void {
    const userId = this.getUserId(); // Utilisez la méthode d'authentification pour obtenir l'ID utilisateur
    if (userId) {
      this.purchaseService.getRevenuesByDeveloper(userId).subscribe(data => {
        this.purchases = data;
        this.totalRevenue = this.purchases.reduce((sum, purchase) => sum + purchase.amount, 0);
        this.loadProjectNames();
        console.log('Purchases:', this.purchases); // Log pour déboguer
        console.log('Project Names:', this.projectNames); // Log pour déboguer
      
      });

    }
     

  }
  loadProjectNames(): void {
    const projectIds = Array.from(new Set(this.purchases.map(purchase => purchase.projectId)));
  
    projectIds.forEach(id => {
      this.purchaseService.getProjectNameById(id).subscribe(
        name => {
          console.log(`Project ID: ${id}, Project Name: ${name}`); // Ajoutez ce log pour vérifier la réponse
          this.projectNames[id] = name;
        },
        error => {
          console.error(`Error fetching project name for ID: ${id}`, error);
        }
      );
    });
  }
  
  
  getUserId(): number | null {
    // Récupérez l'ID de l'utilisateur connecté depuis le service d'authentification
    return Number(localStorage.getItem('userId'));
  }
}
