import {Injectable} from '@angular/core';
import {environment} from '../../environments/environment';
import {HttpClient, HttpParams} from '@angular/common/http';
import {Good} from '../models/good.model';


@Injectable({
  providedIn: 'root'
})
export class OrdersService {

  private apiUrl = environment.apiUrl || 'http://localhost:8080/api/v1';



  constructor(private http: HttpClient) {
  }

  



  getOrders(page: number, goodsPerPage: number) {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('page-size', goodsPerPage.toString());

    return this.http
      .get<{ numPages: number, goods: Good[] }>(
        this.apiUrl + '/all-orders', {params}
      );
  }

  getOrder(orderId: number) {
    return this.http
      .get(`${this.apiUrl}/orders/${orderId}`);
  }

  getOrdersStatuses() {
    return this.http
      .get(`${this.apiUrl}/order-statuses`);
  }

  updateOrder(orderId: number, statusId: number) {
    return this.http.put(`${this.apiUrl}/order/change-status`, {orderId, statusId});
  }

  getUserOrders(page: number, goodsPerPage: number) {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('page-size', goodsPerPage.toString());

    return this.http
      .get<{ numPages: number, goods: Good[] }>(
        this.apiUrl + '/order-history/user', {params}
      );
  }

  repeatOrder(orderId: number, bonuses: number, comment: string) {
    return this.http.put(
      `${this.apiUrl}/order/repeat`,
      {
        orderId,
        bonuses,
        comment
      }
    );
  }


  sendGuestOrder(guestOrder ){
    return this.http.post(
      `${this.apiUrl}/order/guest`,
        guestOrder
    );

  }


  sendUserOrder(userOrder ){
    return this.http.post(
      `${this.apiUrl}/order/user`,
        userOrder
    );




  }

}
