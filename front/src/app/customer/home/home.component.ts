import { Component, OnInit } from '@angular/core';
import { CustomerService } from '../../services/customer.service';
import { ProjectService } from '../../services/project.service';
import { AuthService } from '../../services/auth.service';
import { Router, ActivatedRoute } from '@angular/router';
import Swal from 'sweetalert2';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {
  projects: any[] = [];
  videos: { [key: number]: string } = {};
  developerImages: { [key: number]: string } = {};
  paymentStatus: string | null = null;
  paymentMessage: string | null = null;
  private messageDisplayed: boolean = false; // Ajoutez une variable d'état

  constructor(
    private customerService: CustomerService,
    private projectService: ProjectService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  async ngOnInit(): Promise<void> {
    this.loadProjects();
    this.checkPaymentStatus();
  }

  loadProjects(): void {
    this.customerService.getProjects().subscribe((data: any[]) => {
      this.projects = data;
      this.projects.forEach(project => {
        this.loadVideo(project.id);
        this.loadDeveloperImage(project.userId);
      });
    });
  }

  loadDeveloperImage(userId: number): void {
    this.customerService.getUserImage(userId).subscribe(imageBlob => {
      const imageUrl = URL.createObjectURL(imageBlob);
      this.developerImages[userId] = imageUrl;
    });
  }

  loadVideo(projectId: number): void {
    this.customerService.getProjectVideo(projectId).subscribe((blob: Blob) => {
      const url = URL.createObjectURL(blob);
      this.videos[projectId] = url;
    });
  }

  checkPaymentStatus(): void {
    this.route.queryParams.subscribe(params => {
      this.paymentStatus = params['status'] || null;
      this.paymentMessage = params['message'] || null;
      
      // Afficher le message seulement si cela n'a pas déjà été fait
      if (this.paymentStatus && this.paymentMessage && !this.messageDisplayed) {
        Swal.fire({
          title: this.paymentStatus.charAt(0).toUpperCase() + this.paymentStatus.slice(1),
          text: this.paymentMessage,
          icon: this.paymentStatus === 'success' ? 'success' : 'error',
          confirmButtonText: 'OK'
        }).then(() => {
          this.messageDisplayed = true; // Marquer le message comme affiché
          // Nettoyer les paramètres de l'URL après affichage
          history.replaceState(null, '', this.router.url.split('?')[0]);
        });
      }
    });
  }

  purchaseProject(projectId: number, amount: number): void {
    const userId = this.authService.getUserId();
    if (!userId) {
      console.error('Utilisateur non authentifié');
      return;
    }

    this.router.navigate(['customer/payment'], { queryParams: { amount, projectId } });
  }
}
