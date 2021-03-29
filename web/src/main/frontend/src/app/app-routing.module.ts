import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {GoodsListComponent} from './goods-list/goods-list.component';
import {GoodItemComponent} from './good-item/good-item.component';
import {CheckoutComponent} from './checkout/checkout.component';

import {UserComponent} from './user/user.component';
import {PharmacistComponent} from './pharmacist/pharmacist.component';
import {CategoriesComponent} from './categories/categories.component';
import {FakListComponent} from './fak-list/fak-list.component';
import {ActiveComponentsComponent} from './activecomponents/activecomponents.component';

import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';

import {SigninComponent} from './signin/signin.component';
import {SignupComponent} from './signup/signup.component';
import {AuthGuardService} from './services/auth-guard.service';
import {Role} from './models/roles';
import {ProfileComponent} from './profile/profile.component';
import {EmployeeMainComponent} from './employee-main/employee-main.component';
import {FakComponent} from './fak/fak.component';
import {ChangePasswordComponent} from "./change-password/change-password.component";

const routes: Routes = [
  {
    path: '', component: UserComponent,
    children: [
      {
        path: '',
        redirectTo: 'goods',
        pathMatch: 'full'
      },
      {path: 'goods', component: GoodsListComponent},
      {path: 'goods/:goodId', component: GoodItemComponent},
      {path: 'checkout', component: CheckoutComponent},
      {path: 'categories', component: CategoriesComponent },
      { path: 'kits', component: FakListComponent },
      { path: 'kits/:kitId', component: FakComponent },
      { path: 'active', component : ActiveComponentsComponent},
      { path: 'active/:letter', component : ActiveComponentsComponent},
      { path: 'goods/active-component/:componentId', component : GoodsListComponent},

      {path: 'checkout', component: CheckoutComponent},

      {path: 'shopping-cart', component: ShoppingCartComponent},

      {path: 'profile', component: ProfileComponent,
        canActivate: [AuthGuardService], data: {roles: [Role.USER]}}
    ],
  },
  {
    path: 'employee', component: EmployeeMainComponent,
    canActivate: [AuthGuardService], data: {roles: [Role.EMPLOYEE]},
    children: [
      {
        path: '',
        redirectTo: 'orders',
        pathMatch: 'full'
      },
      {
        path: 'orders', component: PharmacistComponent, pathMatch: 'full',
        canActivate: [AuthGuardService], data: {roles: [Role.EMPLOYEE]}
      },
      {
        path: 'profile', component: ProfileComponent, pathMatch: 'full',
        canActivate: [AuthGuardService], data: {roles: [Role.EMPLOYEE]}
      },
    ]
  },

  {
    path: 'signin', component: SigninComponent
  },
  {
    path: 'signup', component: SignupComponent
  },
  {
    path: 'change-password', component: ChangePasswordComponent
  }
];
@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
