import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CustomerComponent } from './customer.component';
import { HomeComponent } from './home/home.component';
import { PaymentComponent } from './payment/payment.component';
import { PaymentsuccessComponent } from './paymentsuccess/paymentsuccess.component';
import { LayoutComponent } from './layout/layout.component';
import { UpdateprofileComponent } from './updateprofile/updateprofile.component';
import { PurchaseComponent } from './purchase/purchase.component';
 
const routes: Routes = [
  {
    path: '',
    component: LayoutComponent,
    children: [
      { path: 'home', component: HomeComponent },
      { path: 'payment', component: PaymentComponent },
      { path: 'paypal-success/:projectId/:userId/:paymentId/:token/:PayerID', component: PaymentsuccessComponent } ,// Exemple pour les routes dynamiques
      { path: 'update-profile', component: UpdateprofileComponent } // Nouvelle route pour la mise à jour du profil
      ,{ path: 'purchase', component: PurchaseComponent },

     ]
  }
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule]
})
export class CustomerRoutingModule { }
