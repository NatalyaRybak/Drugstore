import { Component, OnInit } from '@angular/core';
import {LocalStorageService} from '../services/local-storage.service';
import {CartGood} from "../models/cart-good.model"
import { MatDialog } from '@angular/material';
import { AuthenticationService } from '../services/authentication.service';
import { BonusesComponent } from '../bonuses/bonuses.component';


@Component({
  selector: 'app-shopping-cart',
  templateUrl: './shopping-cart.component.html',
  styleUrls: ['./shopping-cart.component.scss']
})
export class ShoppingCartComponent implements OnInit {

  constructor(
    private localStorage: LocalStorageService,
    public dialog: MatDialog,
    private authenticationService: AuthenticationService
    ) { }

  itemsFromStorage: CartGood[];
  kitsFromStorage: CartGood[];

  all: CartGood[];
  total;
  subtotal;
  discount ;
  user;

  isLoggedIn(){

    if (this.user != null){
      return true;
    }
    return false;
  }

  ngOnInit() {
    this.user = this.authenticationService.currentUserValue;
    this.discount = 0;
    this.itemsFromStorage = this.localStorage.itemsArray;
    this.kitsFromStorage = this.localStorage.kitsArray;
    this.subtotal = this.localStorage.subtotal;
    this.total = this.localStorage.total;
    this.localStorage.onRemoveClick.subscribe(arr => this.itemsFromStorage = arr);
    this.localStorage.onKitRemoveClick.subscribe(arr => this.kitsFromStorage = arr);
    this.localStorage.onPlusMinusClick.subscribe(t => {this.subtotal = t, this.total = t - this.discount});
    // this.authenticationService.onBonusesClick.subscribe(b => this.discount = b);


  }

  onRemoveClick(good, n){
    this.localStorage.removeNGoodfromStorage(good,n);
  }

  onMinusClick(good){
    this.localStorage.removeNGoodfromStorage(good,1);
  }
  
  onPlusClick(good){
    this.localStorage.addNGoodToStorage(good,1);
  }

  onKitRemoveClick(kit, n){
    this.localStorage.removeNKitsFromStorage(kit,n);
  }

  onKitMinusClick(kit){
    this.localStorage.removeNKitsFromStorage(kit,1);
  }
  
  onKitPlusClick(kit){
    this.localStorage.addNKitsToStorage(kit,1);
  }

  openDialog(): void {
    const dialogRef = this.dialog.open(BonusesComponent, {
      width: '500px',
    });

     dialogRef.afterClosed().subscribe(result => {
       console.log(result.data.bonuses);
       this.discount = result.data.bonuses;
       this.total -= this.discount;
       this.authenticationService.bonusesToUse = result.data.bonuses;
      
    });
  }


}
