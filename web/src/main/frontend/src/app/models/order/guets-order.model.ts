

export interface GuestOrder{
    userName: string;
    userSurname: string;
    userEmail: string;
    userPhone: string;
    comment: string;
    orderLines: OrderLines[];
}

export interface UserOrder{
  bonusesToUse: number;
  userPhone: string;
  comment: string;
  orderLines: OrderLines[];
}

export interface OrderLines{
    goodId: number;
    amount: number;
  }
