package com.kmadrugstore.service;

import com.kmadrugstore.dto.*;
import com.kmadrugstore.entity.Order;
import com.kmadrugstore.entity.OrderLine;
import com.kmadrugstore.entity.OrderStatus;
import com.kmadrugstore.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IOrderService {

    DetailedOrderDTO placeOrder(final GuestOrderDTO order);

    DetailedOrderDTO placeOrder(final UserOrderDTO orderDto, final User user);

    Page<DetailedOrderDTO> findAllOrders(final Pageable pageable);

    Order findOrderById(final int id);

    Page<DetailedOrderDTO> findByStatus(final Pageable pageable, final int status);

    List<OrderLine> getGoodsFromOrder(final int id);

    Order changeStatus(final Order order, final int status);

    List<OrderStatus> getAllStatuses();

    Long quantityOfAllOrders();

    Long quantityOfAllOrdersByStatus(String status);

    DetailedOrderDTO orderToDetailedOrderDTO(final Order order);

    Page<DetailedOrderDTO> getOrdersHistory(Pageable pageable, final int userId);

    void cancelExpiredOrders();

    DetailedOrderDTO changeStatus(final OrderStatusDTO osDTO);

    DetailedOrderDTO repeatOrder(final OrderToRepeatDTO newOrderDTO, final User user);
}
