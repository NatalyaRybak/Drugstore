import {OrderLine} from "./order-line.model";
import {OrderUser} from "./order-user.model";
import {OrderStatus} from "./order-status.model";

export interface Order {
  id: number;
  date: string;
  total: number;
  bonusesToUse: number;
  bonusesToGet: number;
  phone: string;
  comment: string;
  status: OrderStatus;
  user: OrderUser;
  orderLines: OrderLine[];
}
