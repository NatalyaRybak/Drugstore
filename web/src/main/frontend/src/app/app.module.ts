import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { StorageServiceModule } from 'ngx-webstorage-service';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {GoodsListComponent} from './goods-list/goods-list.component';
import {
  MatButtonModule,
  MatCardModule,
  MatInputModule,
  MatMenuModule,
  MatToolbarModule,
  MatPaginatorModule,
  MatTableModule,
  MatProgressSpinnerModule
} from '@angular/material';
import {HTTP_INTERCEPTORS, HttpClient, HttpClientModule} from '@angular/common/http';
import { HeaderComponent } from './header/header.component';
import {MatIconModule} from '@angular/material/icon';
import {MatChipsModule} from '@angular/material/chips';
import {MatGridListModule} from '@angular/material/grid-list';
import {MatDividerModule} from '@angular/material/divider';
import { MatDialogModule } from '@angular/material';
import { MatSnackBarModule } from '@angular/material/';
import { GoodItemComponent } from './good-item/good-item.component';
import {FlexLayoutModule} from '@angular/flex-layout';
import {MatSidenavModule} from '@angular/material/sidenav';
import {MatSelectModule} from '@angular/material/select';
import { CheckoutComponent , Redirect} from './checkout/checkout.component';
import { UserComponent } from './user/user.component';
import { PharmacistComponent } from './pharmacist/pharmacist.component';
import { PhrHeaderComponent } from './phr-header/phr-header.component';
import { PhrTableComponent } from './phr-table/phr-table.component';
import { OrderDetailsComponent } from './order-details/order-details.component';
import { CategoriesComponent } from './categories/categories.component';
import { FakListComponent } from './fak-list/fak-list.component';
import {ActiveComponentsComponent} from './activecomponents/activecomponents.component';
import {MatListModule} from '@angular/material/list';
import { NoopAnimationsModule} from '@angular/platform-browser/animations';

import { ShoppingCartComponent } from './shopping-cart/shopping-cart.component';

import { SigninComponent } from './signin/signin.component';
import { SignupComponent } from './signup/signup.component';
import { ForgotPswComponent } from './forgot-psw/forgot-psw.component';
import {TokenInterceptor} from './interceptors/token.interceptor';
import {FormsModule, ReactiveFormsModule} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import { MatTabsModule } from '@angular/material';


import { HashLocationStrategy, LocationStrategy } from '@angular/common';
import { ProfileComponent } from './profile/profile.component';
import { MakeOrderDialogComponent } from './make-order-dialog/make-order-dialog.component';
import { EmployeeMainComponent } from './employee-main/employee-main.component';
import { FakComponent } from './fak/fak.component';
import { ChangePasswordComponent } from './change-password/change-password.component';
import { InfoDialogComponent } from './info-dialog/info-dialog.component';
import { BonusesComponent } from './bonuses/bonuses.component';


// tslint:disable-next-line:max-line-length
// import {StyleUtils,StylesheetMap,MediaMarshaller,ɵMatchMedia,BreakPointRegistry,PrintHook,LayoutStyleBuilder,FlexStyleBuilder,ShowHideStyleBuilder,FlexOrderStyleBuilder} from "@angular/flex-layout";
@NgModule({
  declarations: [
    AppComponent,
    GoodsListComponent,
    HeaderComponent,
    GoodItemComponent,
    CheckoutComponent,
    UserComponent,
    PharmacistComponent,
    PhrHeaderComponent,
    PhrTableComponent,
    OrderDetailsComponent,
    CategoriesComponent,
    FakListComponent,
    ActiveComponentsComponent,

    ShoppingCartComponent,

    SigninComponent,
    SignupComponent,
    ForgotPswComponent,
    ProfileComponent,
    MakeOrderDialogComponent,
    EmployeeMainComponent,
    FakComponent,
    ChangePasswordComponent,
    InfoDialogComponent,
    BonusesComponent,
    Redirect

  ],
  imports: [
    NoopAnimationsModule,
    StorageServiceModule,
    AppRoutingModule,
    FlexLayoutModule,
    MatPaginatorModule,
    MatCardModule,
    MatButtonModule,
    MatMenuModule,
    MatInputModule,
    MatToolbarModule,
    MatTableModule,
    HttpClientModule,
    MatIconModule,
    MatChipsModule,
    MatGridListModule,
    MatDividerModule,
    MatSidenavModule,
    MatSelectModule,
    MatDialogModule,
    MatSnackBarModule,
    MatListModule,
    BrowserModule,
    ReactiveFormsModule,
    MatProgressSpinnerModule,
    FormsModule,
    MatFormFieldModule,
    ReactiveFormsModule,
    MatTabsModule
  ],
  providers: [MatDialogModule, HttpClient,
    {
      provide: HTTP_INTERCEPTORS,
      useClass: TokenInterceptor,
      multi: true
    },
    {
      provide: LocationStrategy,
      useClass: HashLocationStrategy
    }
  ],
  bootstrap: [AppComponent],
  entryComponents: [
      OrderDetailsComponent
    , ForgotPswComponent
    , MakeOrderDialogComponent
    , InfoDialogComponent
    , BonusesComponent,
    Redirect
  ]
})
export class AppModule { }
