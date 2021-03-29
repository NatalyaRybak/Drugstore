package com.kmadrugstore.utils;

import com.kmadrugstore.entity.OrderStatus;

public class OrderStatusFactory {

    private static final OrderStatus pendingStatus =
            new OrderStatus(Constants.PENDING_STATUS_ID,
                    Constants.PENDING_STATUS_STRING);
    private static final OrderStatus completedStatus =
            new OrderStatus(Constants.COMPLETED_STATUS_ID,
                    Constants.COMPLETED_STATUS_STRING);
    private static final OrderStatus paidStatus =
            new OrderStatus(Constants.PAID_STATUS_ID,
                    Constants.PAID_STATUS_STRING);
    private static final OrderStatus cancelledStatus =
            new OrderStatus(Constants.CANCELLED_STATUS_ID,
                    Constants.CANCELLED_STATUS_STRING);

    public static OrderStatus pending() {
        return pendingStatus;
    }

    public static OrderStatus completed() {
        return completedStatus;
    }

    public static OrderStatus paid() {
        return paidStatus;
    }

    public static OrderStatus cancelled() {
        return cancelledStatus;
    }

    public static String getStatusName(final int id) {
        switch(id){
            case Constants.PENDING_STATUS_ID:
                return Constants.PENDING_STATUS_STRING;
            case Constants.COMPLETED_STATUS_ID:
                return Constants.COMPLETED_STATUS_STRING;
            case Constants.PAID_STATUS_ID:
                return Constants.PAID_STATUS_STRING;
            case Constants.CANCELLED_STATUS_ID:
                return Constants.CANCELLED_STATUS_STRING;
            default:
                return Constants.PENDING_STATUS_STRING;
        }
    }

}
