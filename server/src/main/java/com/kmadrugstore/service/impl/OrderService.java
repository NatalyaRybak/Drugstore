package com.kmadrugstore.service.impl;

import com.kmadrugstore.dto.*;
import com.kmadrugstore.entity.*;
import com.kmadrugstore.exception.EmailIsUsedException;
import com.kmadrugstore.exception.NotEnoughBonusesException;
import com.kmadrugstore.exception.OrderNotFoundException;
import com.kmadrugstore.exception.StatusNotFoundException;
import com.kmadrugstore.repository.IOrderLineRepository;
import com.kmadrugstore.repository.IOrderRepository;
import com.kmadrugstore.repository.IOrderStatusRepository;
import com.kmadrugstore.repository.IUserRepository;
import com.kmadrugstore.service.IEmailService;
import com.kmadrugstore.service.IGoodService;
import com.kmadrugstore.service.IOrderService;
import com.kmadrugstore.service.IUserService;
import com.kmadrugstore.utils.Constants;
import com.kmadrugstore.utils.OrderStatusFactory;
import com.kmadrugstore.utils.RoleFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IOrderStatusRepository orderStatusRepository;
    private final IOrderLineRepository orderLineRepository;
    private final IUserRepository userRepository;
    private final IUserService userService;
    private final IGoodService goodService;
    private final IEmailService mailService;

    @Override
    public DetailedOrderDTO placeOrder(final GuestOrderDTO orderDto) {
        if (userRepository.findByEmail(orderDto.getUserEmail()).isPresent()) {
            throw new EmailIsUsedException();
        }

        // create user (or get existing)
        User guest = userService.getGuest(orderDto.getUserName(),
                orderDto.getUserSurname(), orderDto.getUserEmail());

        return doPlaceOrder(guest, orderDto.getOrderLines(),
                orderDto.getComment(), orderDto.getUserPhone(), 0);
    }

    @Override
    public DetailedOrderDTO placeOrder(final UserOrderDTO orderDto,
                                       final User user) {
        return doPlaceOrder(user, orderDto.getOrderLines(),
                orderDto.getComment(), orderDto.getUserPhone(),
                orderDto.getBonusesToUse());
    }

    @Override
    public DetailedOrderDTO repeatOrder(final OrderToRepeatDTO newOrderDTO, final User user){

        Order orderToRepeat = findOrderById(newOrderDTO.getOrderId());

        List<OrderLine> orderLinesFromOrderToRepeat = getGoodsFromOrder(newOrderDTO.getOrderId());
        List<OrderLineDTO> orderLinesFromOrderToRepeatDTO = orderLineToOrderLineDTO(orderLinesFromOrderToRepeat);

        return doPlaceOrder(user, orderLinesFromOrderToRepeatDTO,
                newOrderDTO.getComment(), orderToRepeat.getPhone(),
                newOrderDTO.getBonuses());
    }

    private DetailedOrderDTO doPlaceOrder(
            final User user,
            final List<OrderLineDTO> orderLines,
            final String comment,
            final String phone,
            final int bonusesToUse) {
        // calculate totals
        List<BigDecimal> subtotals = getSubtotals(orderLines);
        BigDecimal total = getTotal(subtotals);

        // calculate bonuses
        int bonusesUsed = 0;
        int bonusesEarned = 0;
        if (user.getRole().equals(RoleFactory.user())) {
            bonusesUsed = calculateBonusesUsed(bonusesToUse, total);
            reserveBonuses(bonusesUsed, user);
            total = total.subtract(BigDecimal.valueOf(bonusesUsed));
            bonusesEarned = calculateBonusesEarned(total);
            System.out.println("bonuses earned: " + bonusesEarned);
        }

        // create and save order and order lines
        LocalDateTime now = LocalDateTime.now();
        Order order = Order.builder()
                .date(now)
                .expirationDate(now.plusDays(Constants.ORDER_VALIDITY_IN_DAYS))
                .total(total)
                .bonusesToUse(bonusesUsed)
                .bonusesToGet(bonusesEarned)
                .phone(phone)
                .comment(comment)
                .user(user)
                .status(OrderStatusFactory.pending())
                .build();
        Order savedOrder = orderRepository.save(order);
        List<OrderLine> lines = saveOrderLines(orderLines,
                subtotals, savedOrder.getId());

        reserveGoods(savedOrder.getId());

        sendPendingOrderEmail(user.getName(), user.getSurname(),
                user.getEmail());

        return orderToDetailedOrderDTO(savedOrder, lines);
    }

    private BigDecimal getTotal(final List<BigDecimal> subtotals) {
        return subtotals.stream().reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private int calculateBonusesUsed(final int bonusesToUse,
                                     final BigDecimal total) {
        int intTotal = total.setScale(0, BigDecimal.ROUND_FLOOR).intValue();
        return bonusesToUse <= intTotal ? bonusesToUse : intTotal;
    }

    private int calculateBonusesEarned(final BigDecimal total) {
        double roundedTotal =
                total.setScale(0, BigDecimal.ROUND_FLOOR).intValue();
        return (int) Math.floor(roundedTotal / (double) Constants.AMOUNT_FOR_BONUS);
    }

    private void reserveBonuses(final int bonuses, final User user) {
        if (user.getBonuses() < bonuses) {
            throw new NotEnoughBonusesException();
        }
        if (bonuses > 0) {
            userService.decreaseBonusesBy(user, bonuses);
        }
    }

    private void unReserveBonuses(final Order order) {
        if (order.getBonusesToUse() > 0) {
            userService.increaseBonusesBy(order.getUser(),
                    order.getBonusesToUse());
        }
    }

    private void reserveGoods(final int orderId) {
        List<OrderLine> lines = getGoodsFromOrder(orderId);
        for (OrderLine line : lines) {
            goodService.decreaseBy(line.getGood().getId(), line.getNumInOrder());
        }
    }

    private void unReserveGoods(final int orderId) {
        List<OrderLine> lines = getGoodsFromOrder(orderId);
        for (OrderLine line : lines) {
            goodService.increaseBy(line.getGood().getId(), line.getNumInOrder());
        }
    }

    @Override
    public Page<DetailedOrderDTO> findAllOrders(Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findAll(pageable);
        return ordersPage.map(this::orderToDetailedOrderDTO);
    }

    @Override
    public Page<DetailedOrderDTO> getOrdersHistory(Pageable pageable, final int userId) {
        Page<Order> ordersPage = orderRepository.findByUser_Id(userId,
                pageable);
        return ordersPage.map(this::orderToDetailedOrderDTO);
    }

    @Override
    public Order findOrderById(int id) {
        return orderRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    @Override
    public Page<DetailedOrderDTO> findByStatus(Pageable pageable, int status) {
        Page<Order> orderPage = orderRepository.findByStatus_Id(status,
                pageable);
        return orderPage.map(this::orderToDetailedOrderDTO);
    }

    @Override
    public List<OrderLine> getGoodsFromOrder(int id) {
        return orderLineRepository.findByOrder_Id(id);
    }

    @Override
    public Order changeStatus(final Order order, final int status) {
        //new Order();
        Order changedOrder;
        switch (status) {
            case Constants.COMPLETED_STATUS_ID:
                doCompleteOrder(order);
                changedOrder = setNewStatusForOrder(order,
                        OrderStatusFactory.completed());
                break;
            case Constants.PAID_STATUS_ID:
                doPayOrder(order);
                changedOrder = setNewStatusForOrder(order,
                        OrderStatusFactory.paid());
                break;
            case Constants.CANCELLED_STATUS_ID:
                doCancelOrder(order);
                changedOrder = setNewStatusForOrder(order,
                        OrderStatusFactory.cancelled());
                break;
            default:
                throw new StatusNotFoundException();
        }
        return changedOrder;
    }

    @Override
    public List<OrderStatus> getAllStatuses() {
        return orderStatusRepository.findAll();
    }

    @Override
    public Long quantityOfAllOrders() {
        return orderRepository.count();
    }


    @Override
    public Long quantityOfAllOrdersByStatus(String status) {
        return orderRepository.countByStatus_Id(Integer.parseInt(status));
    }

    @Override
    public DetailedOrderDTO changeStatus(final OrderStatusDTO osDTO){
        Order orderToChange = findOrderById(osDTO.getOrderId());
        Order changedOrder = changeStatus(orderToChange,
                osDTO.getStatusId());

        return (orderToDetailedOrderDTO(changedOrder));
    }



    @Override
    public DetailedOrderDTO orderToDetailedOrderDTO(final Order order) {
        return orderToDetailedOrderDTO(order,
                getGoodsFromOrder(order.getId()));
    }

    @Override
    public void cancelExpiredOrders() {
        orderRepository.findAllByExpirationDateBeforeAndStatus_IdIn(
                LocalDateTime.now(), Arrays.asList(
                        Constants.PENDING_STATUS_ID,
                        Constants.COMPLETED_STATUS_ID))
                .forEach(o -> changeStatus(o, Constants.CANCELLED_STATUS_ID));
    }

    private void doCompleteOrder(final Order order) {
        sendCompletedOrderEmail(order.getUser().getEmail(), order.getId(),
                order.getTotal());
    }

    private void doPayOrder(final Order order) {
        if (order.getUser().getRole().equals(RoleFactory.user())) {
            if (order.getBonusesToGet() > 0) {
                userService.increaseBonusesBy(
                        order.getUser(), order.getBonusesToGet());
            }
        }
        sendPaidOrderEmail(order.getUser().getEmail(), order.getBonusesToGet());
    }

    private void doCancelOrder(final Order order) {
        unReserveGoods(order.getId());
        unReserveBonuses(order);

        sendCancelledOrderEmail(order.getUser().getName(),
                order.getUser().getSurname(), order.getUser().getEmail(),
                order.getId());
    }


    private Order setNewStatusForOrder(final Order order,
                                       final OrderStatus status) {

        order.setStatus(status);

        orderRepository.save(order);
        return order;
    }

    private List<BigDecimal> getSubtotals(final List<OrderLineDTO> orderLines) {
        List<BigDecimal> subtotals = new ArrayList<>();
        for (OrderLineDTO line : orderLines) {
            BigDecimal price = goodService.getPriceById(line.getGoodId());
            subtotals.add(price.multiply(BigDecimal.valueOf(line.getAmount())));
        }
        return subtotals;
    }

    private List<OrderLine> saveOrderLines(final List<OrderLineDTO> orderLineDtos,
                                           final List<BigDecimal> totals,
                                           final int orderId) {
        List<OrderLine> lines = new ArrayList<>();
        for (int i = 0; i < orderLineDtos.size(); ++i) {
            lines.add(saveOrderLine(orderLineDtos.get(i), totals.get(i),
                    orderId));
        }
        return lines;
    }

    private OrderLine saveOrderLine(final OrderLineDTO orderLineDto,
                                    final BigDecimal total, final int orderId) {
        OrderLine orderLine = OrderLine.builder()
                .numInOrder(orderLineDto.getAmount())
                .sum(total)
                .good(new Good(orderLineDto.getGoodId()))
                .order(new Order(orderId))
                .build();
        orderLineRepository.save(orderLine);
        return orderLine;
    }

    private List<OrderLineDTO> orderLineToOrderLineDTO(final List<OrderLine> lines)
    {
        List<OrderLineDTO> newLines = new ArrayList<OrderLineDTO>();

        for (int i = 0; i < lines.size(); i++) {
            OrderLineDTO olDTO = new OrderLineDTO();
            olDTO.setAmount(lines.get(i).getNumInOrder());
            olDTO.setGoodId(lines.get(i).getGood().getId());
            newLines.add(olDTO);
        }

        return newLines;
    }


    /************** EMAIL ****************/

    private void sendPendingOrderEmail(final String userName,
                                       final String userSurname,
                                       final String email) {
        String mes = String.format(Constants.PENDING_STATUS_EMAIL, userName,
                userSurname);
        sendOrderInfoToEmail(email, mes);
    }

    private void sendCompletedOrderEmail(final String email,
                                         final int orderId,
                                         final BigDecimal total) {
        String mes = String.format(Constants.COMPLETED_STATUS_EMAIL,
                String.valueOf(orderId),
                String.valueOf(total));
        sendOrderInfoToEmail(email, mes);
    }

    private void sendPaidOrderEmail(final String email, final int newBonuses) {
        String mes = String.format(Constants.PAID_STATUS_EMAIL,
                String.valueOf(newBonuses));
        sendOrderInfoToEmail(email, mes);
    }

    private void sendCancelledOrderEmail(final String userName,
                                         final String userSurname,
                                         final String email,
                                         final int orderId) {
        String mes = String.format(Constants.CANCELLED_STATUS_EMAIL,
                userName, userSurname, orderId);
        sendOrderInfoToEmail(email, mes);
    }

    private void sendOrderInfoToEmail(final String email, final String mes) {
        mailService.sendMessageToEmail(email, Constants.ORDER_EMAIL_SUBJECT, mes);
    }


    /************** MAPPERS ***************/

    private DetailedOrderDTO orderToDetailedOrderDTO(final Order order,
                                                     final List<OrderLine> list) {
        List<DetailedOrderLineDTO> newLines = new ArrayList<>();
        for (OrderLine orderLine : list) {
            newLines.add(orderLineToDetailedOrderLineDTO(orderLine));
        }

        return DetailedOrderDTO.builder()
                .id(order.getId())
                .date(order.getDate())
                .total(order.getTotal())
                .bonusesToUse(order.getBonusesToUse())
                .bonusesToGet(order.getBonusesToGet())
                .phone(order.getPhone())
                .comment(order.getComment())
                .status(OrderStatusNameDTO
                        .builder()
                        .statusId(order.getStatus().getId())
                        .orderId(order.getId())
                        .statusName(order.getStatus().getName())
                        .build())
                .user(UserDTO.builder()
                        .id(order.getUser().getId())
                        .name(order.getUser().getName())
                        .surname(order.getUser().getSurname())
                        .email(order.getUser().getEmail())
                        .bonuses(order.getUser().getBonuses())
                        .role(order.getUser().getRole())
                        .build())
                .orderLines(newLines)
                .build();
    }

    private DetailedOrderLineDTO orderLineToDetailedOrderLineDTO(OrderLine line) {
        GoodDTO g =
                goodService.goodToGoodDTO(goodService.findGoodById(line.getGood().getId()));
        return DetailedOrderLineDTO.builder()
                .amount(line.getNumInOrder())
                .sum(line.getSum().doubleValue())
                .good(g)
                .build();
    }


}
