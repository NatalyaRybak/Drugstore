import {OrderGood} from "./order-good.model";

export interface OrderLine {
  good: OrderGood;
  amount: number;
  sum: number;
}

