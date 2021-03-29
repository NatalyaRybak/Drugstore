import {Component, OnInit, ViewChild, ElementRef, Inject} from '@angular/core';
import {OrdersService} from '../services/orders.service';
import {LocalStorageService} from '../services/local-storage.service';
import {OrderLines, UserOrder} from '../models/order/guets-order.model';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {GuestOrder} from '../models/order/guets-order.model';
import {AuthenticationService} from '../services/authentication.service';
import {UserInfo} from '../models/userInfo';
import {MatDialog, MAT_DIALOG_DATA} from '@angular/material/dialog';

import {MatSnackBar} from "@angular/material";

@Component({
  selector: 'app-checkout',
  templateUrl: './checkout.component.html',
  styleUrls: ['./checkout.component.scss']
})
export class CheckoutComponent implements OnInit {

  form: FormGroup;

  isLoadingResults = false;
  orderList: OrderLines[];
  bonuses: number;
  user: UserInfo;

  constructor(private _snackBar: MatSnackBar,
              private orderService: OrdersService,
              private localStorage: LocalStorageService,
              public authService: AuthenticationService,
              public dialog: MatDialog,
              private fb: FormBuilder
  ) {}

  ngOnInit() {
    this.user = this.authService.currentUserValue;

    if(this.user != null){
      this.form = this.fb.group({
        email: [this.user.email, [Validators.email, Validators.required]],
        userName: [this.user.name, Validators.required],
        userSurname: [this.user.surname, Validators.required],
        userPhone: ['', Validators.required],
        comment: ['']
      });
    }
    else{
      this.form = this.fb.group({
        email: ['', [Validators.email, Validators.required]],
        userName: ['', Validators.required],
        userSurname: ['', Validators.required],
        userPhone: ['', Validators.required],
        comment: ['']
      });
    }
  }


  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 2000,
    });
  }


  onOrderClick() {
    if(this.form.valid) {
      this.getGoods();

      if (this.user != null) {
        this.bonuses = this.authService.bonusesToUse;

        const userOrder: UserOrder = {
          bonusesToUse: this.bonuses,
          userPhone: this.form.get('userPhone').value,
          comment: this.form.get('comment').value,
          orderLines: this.orderList
        };

        this.isLoadingResults = true;


        this.orderService.sendUserOrder(userOrder).subscribe((data: any) => {
          this.authService.updateUser(this.bonuses);
          this.isLoadingResults = false;
          this.localStorage.onCheckOut();
          this.openDialog();
        }, error => {
          console.log(error);
          this.isLoadingResults = false;
        });

      } else {

        const guestOrder: GuestOrder = {
          userName: this.form.get('userName').value,
          userSurname:this.form.get('userSurname').value,
          userEmail: this.form.get('email').value,
          userPhone: this.form.get('userPhone').value,
          comment: this.form.get('comment').value,
          orderLines: this.orderList
        };

        this.isLoadingResults = true;
        this.orderService.sendGuestOrder(guestOrder).subscribe((data: any) => {
          this.isLoadingResults = false;
          this.localStorage.onCheckOut();
          this.openDialog();
        }, error => {
          console.log(error);
          this.isLoadingResults = false;
        });

      }
    }
  }

  getGoods() {
    this.orderList = [];

    for (let i = 0; i < this.localStorage.itemsArray.length; i++) {
      //if(this.localStorage.itemsArray[i].object)
      this.orderList[i] = ({
        goodId: this.localStorage.itemsArray[i].object.id,
        amount: this.localStorage.itemsArray[i].numOrdered
      });

    }
    for (let i = 0; i < this.localStorage.kitsArray.length; i++) {
      for (let j = 0; j < this.localStorage.kitsArray[i].object.kitLines.length; j++) {
        let klId = this.localStorage.kitsArray[i].object.kitLines[j].good.id;
        let klnO = this.localStorage.kitsArray[i].object.kitLines[j].quantity * this.localStorage.kitsArray[i].numOrdered;
        this.orderList.push({goodId: klId, amount: klnO});
      }

    }

  }

  openDialog(): void {
    this.dialog.open(Redirect, {
      width: '500px',
    });
  }

}


@Component({
  selector: 'redirectAfterCheckout',
  template: `

    <p>Thank you for the order. Detailed information was sent to your email.</p>
    <button mat-button routerLink="/goods" mat-dialog-close>Ok</button>

  `
})
export class Redirect {


}


