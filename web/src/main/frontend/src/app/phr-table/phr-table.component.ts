import {Component, OnInit, ViewChild} from '@angular/core';
import {OrdersService} from '../services/orders.service';
import {Order} from '../models/order/order.model';
import {MatDialog, MatPaginator, MatSnackBar} from '@angular/material';
import {OrderDetailsComponent} from '../order-details/order-details.component';
import {FormBuilder, FormGroup} from '@angular/forms';
import {AuthenticationService} from '../services/authentication.service';
import {MakeOrderDialogComponent} from '../make-order-dialog/make-order-dialog.component';

@Component({
  selector: 'app-phr-table',
  templateUrl: './phr-table.component.html',
  styleUrls: ['./phr-table.component.scss']
})
export class PhrTableComponent implements OnInit {
  form: FormGroup;

  displayedColumns: string[] = ['id', 'date', 'total', 'status', 'details'];
  orders: Order[] = [];
  statuses = [];

  totalItems = 0;
  currentPage = 0;
  itemsPerPage = 10;
  pageSizeOptions = [5, 10, 25, 50, 100];

  isLoadingResults = false;

  @ViewChild(MatPaginator, {static: false}) paginator: MatPaginator;

  constructor(
    public ordersService: OrdersService,
    public dialog: MatDialog,
    // tslint:disable-next-line:variable-name
    private _snackBar: MatSnackBar,
    private fb: FormBuilder,
    private authenticationService: AuthenticationService) { }

  ngOnInit() {
    if (this.isEmployee()) {
      this.loadAll();
    } else {
      this.loadUsers();
    }

    this.form = this.fb.group({
      orderId: ''
    });
  }

  isEmployee() {
    return this.authenticationService.isEmployee();
  }

  loadAll() {
    this.loadOrders(this.currentPage, this.itemsPerPage);
    this.loadStatuses();
  }

  loadUsers() {
    this.loadUserOrders(this.currentPage, this.itemsPerPage);
  }

  search() {
    const orderId = this.form.get('orderId').value;
    this.isLoadingResults = true;
    if (orderId) {
      this.ordersService.getOrder(orderId).subscribe((order: any) => {
        this.orders = [order];
        this.isLoadingResults = false;
      });
    } else {
      this.loadOrders(this.currentPage, this.itemsPerPage);
    }
  }

  ngAfterViewInit(): void {
    this.paginator.page.subscribe((data => {
      this.loadOrders(this.paginator.pageIndex, this.paginator.pageSize);
    }));
  }

  openDialog(selectedOrder): void {
    const dialogRef = this.dialog.open(OrderDetailsComponent, {
      width: '900px',
      data: { order: selectedOrder, statuses: this.statuses }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result.status === 'Save') {
        this.isLoadingResults = true;
        this.ordersService.updateOrder(result.data.orderId, result.data.statusId).subscribe((data: Order) => {
          this.isLoadingResults = false;
          this.updateLoaded(result.data.orderId, data);
          this.openSnackBar('Order is saved!', 'Close');
        });
      } else if (result.status === 'Reorder') {
        this.openCheckoutDialog(result.data.orderId);
      }
    });
  }

  openSnackBar(message: string, action: string) {
    this._snackBar.open(message, action, {
      duration: 2000,
    });
  }

  openCheckoutDialog(orderId) {
    const dialogRef = this.dialog.open(MakeOrderDialogComponent, {
      width: '400px',
      data: { orderId }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result.status === 'Ok') {
        this.isLoadingResults = true;
        this.ordersService.repeatOrder(orderId, result.data.bonuses, result.data.comment)
          .subscribe((data: any) => {
            this.orders.unshift(data);
            this.orders = [...this.orders];
            this.substractBonusesFromUser(result.data.bonuses);
            this.openSnackBar('Your order has been saved', 'Close');
            this.isLoadingResults = false;
        });
      }
    });
  }

  updateLoaded(orderId: number, data: Order) {
    const index = this.orders.findIndex((order: Order) => order.id === orderId);
    if (index > -1) {
      this.orders.splice(index, 1, data);
      this.orders = [...this.orders];
    }
  }

  substractBonusesFromUser(usedBonuses) {
    if (usedBonuses) {
      this.authenticationService.updateUser(usedBonuses);
    }
  }

  findStatusNameById(statusId: number) {
    // tslint:disable-next-line:no-shadowed-variable
    const status = this.statuses.find(status => status.id === statusId);
    return status ? status.name : '';
  }

  parseDate(date) {
    const dateObj = new Date(date);
    const dateValue = dateObj.toLocaleDateString('en-US');
    const timeValue = dateObj.toLocaleTimeString('en-US');
    return `${dateValue} ${timeValue}`;
  }

  private loadOrders(page: number, ordersPerPage: number) {
    this.isLoadingResults = true;
    this.ordersService.getOrders(page, ordersPerPage).subscribe((data: any) => {
      this.orders = data.orders;
      this.totalItems = data.numPages * this.itemsPerPage;
      this.isLoadingResults = false;
    });
  }

  private loadUserOrders(page: number, ordersPerPage: number) {
    this.isLoadingResults = true;
    this.ordersService.getUserOrders(page, ordersPerPage).subscribe((data: any) => {
      this.orders = data.orders;
      this.totalItems = data.numPages * this.itemsPerPage;
      this.isLoadingResults = false;
    });
  }

  private loadStatuses() {
    this.ordersService.getOrdersStatuses().subscribe((data: any) => {
      this.statuses = data;
    });
  }
}
