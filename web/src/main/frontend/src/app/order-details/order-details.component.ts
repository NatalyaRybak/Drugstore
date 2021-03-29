import {Component, Inject, OnInit} from '@angular/core';
import {MAT_DIALOG_DATA, MatDialogRef} from '@angular/material';
import {OrdersService} from '../services/orders.service';
import {Order} from '../models/order/order.model';
import {AuthenticationService} from '../services/authentication.service';

@Component({
  selector: 'app-order-details',
  templateUrl: './order-details.component.html',
  styleUrls: ['./order-details.component.scss']
})
export class OrderDetailsComponent implements OnInit {

  public order: Order;
  public statuses;
  public selected;

  constructor(
    private authenticationService: AuthenticationService,
    public ordersService: OrdersService,
    public thisDialogRef: MatDialogRef<OrderDetailsComponent>,
    @Inject(MAT_DIALOG_DATA) public data) {
    thisDialogRef.disableClose = true;
  }

  ngOnInit() {
    this.order = this.data.order;
    this.statuses = this.data.statuses || [];
    if (this.order.status.statusId !== 1) {
      this.statuses = this.statuses.filter(status => status.id !== 1);
    }

    this.selected =  this.order.status.statusId;
  }

  isEmployee() {
    return this.authenticationService.isEmployee();
  }

  async onCloseSave() {
    if (this.order.status.orderId === this.selected) {
      this.thisDialogRef.close({
        status: 'Unchanged'
      });
    } else {
      this.thisDialogRef.close({
        status: 'Save',
        data: {
          orderId: this.order.status.orderId,
          statusId: this.selected
        }
      });
    }
  }

  onCloseCancel() {
    this.thisDialogRef.close({
      status: 'Cancel'
    });
  }

  onReorderBtn() {
    this.thisDialogRef.close({
      status: 'Reorder',
      data: {
        orderId: this.order.id
      }
    });
  }

  parseDate(date) {
    const dateObj = new Date(date);
    const dateValue = dateObj.toLocaleDateString('en-US');
    const timeValue = dateObj.toLocaleTimeString('en-US');
    return `${dateValue} ${timeValue}`;
  }

  // private loadOrder(orderId: number) {
  //   this.ordersService.getOrder(orderId).subscribe((data: any) => {
  //     this.order = data;
  //     console.log(this.order);
  //   });
  // }
}
