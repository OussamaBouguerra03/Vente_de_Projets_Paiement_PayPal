import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { CustomerRoutingModule } from './customer-routing.module';
import { CustomerComponent } from './customer.component';
import { ProfileComponent } from './profile/profile.component';
import { RequestsComponent } from './requests/requests.component';
import { HomeComponent } from './home/home.component';
 import { FormsModule } from '@angular/forms';
import { PaymentComponent } from './payment/payment.component';
import { PaymentsuccessComponent } from './paymentsuccess/paymentsuccess.component';
import { SidebarComponent } from './sidebar/sidebar.component';
import { LayoutComponent } from './layout/layout.component';
import { UpdateprofileComponent } from './updateprofile/updateprofile.component';
import { PurchaseComponent } from './purchase/purchase.component';
 

@NgModule({
  declarations: [
    CustomerComponent,
    ProfileComponent,
    RequestsComponent,
    HomeComponent,
    PaymentComponent,
    PaymentsuccessComponent,
    SidebarComponent,
    LayoutComponent,
    UpdateprofileComponent,
    PurchaseComponent,
   ],
  imports: [
    CommonModule,
    CustomerRoutingModule,
    FormsModule
  ]
})
export class CustomerModule { }
