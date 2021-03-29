package com.kmadrugstore.controller;

import com.kmadrugstore.dto.*;
import com.kmadrugstore.entity.Order;
import com.kmadrugstore.entity.OrderStatus;
import com.kmadrugstore.entity.ShortGood;
import com.kmadrugstore.entity.User;
import com.kmadrugstore.jwt.JwtTokenUtil;
import com.kmadrugstore.service.IOrderService;
import com.kmadrugstore.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;
    private final IUserService userService;
    private final JwtTokenUtil tokenUtil;

    @PreAuthorize("permitAll()")
    @PostMapping("/order/guest")
    public ResponseEntity<DetailedOrderDTO> saveGuestOrder(
            @RequestBody final GuestOrderDTO order) {
        return ok(orderService.placeOrder(order));
    }

    @PreAuthorize("hasAuthority(T(com.kmadrugstore.utils.Constants)" +
            ".USER_ROLE_STRING)")
    @PostMapping("/order/user")
    public ResponseEntity<DetailedOrderDTO> saveUserOrder(
            @RequestBody final UserOrderDTO order, final Principal principal) {
        User user = userService.findByEmail(principal.getName());
        return ok(orderService.placeOrder(order, user));

    }



    @PreAuthorize("hasAuthority(T(com.kmadrugstore.utils.Constants)" +
            ".USER_ROLE_STRING)")
    @GetMapping("/order-history/user")
    public ResponseEntity<Map<Object, Object>> getOrdersHistory(
            @RequestParam(value = "page") final int page,
            @RequestParam("page-size") final int pageSize,
            final Principal principal) {
        Page<DetailedOrderDTO> result;

        User user = userService.findByEmail(principal.getName());
        result = orderService.getOrdersHistory(PageRequest.of(page, pageSize,
                    Sort.by("date").descending()), user.getId());
        Map<Object, Object> model = new HashMap<>();
        model.put("orders", result.getContent());
        model.put("numPages", result.getTotalPages());
        return ok(model);
    }

    @PreAuthorize("hasAuthority(T(com.kmadrugstore.utils.Constants)" +
            ".USER_ROLE_STRING)")
    @PutMapping("/order/repeat")
    public ResponseEntity<DetailedOrderDTO>  repeatOrder (
            @RequestBody final OrderToRepeatDTO order, final Principal principal) {

        User user = userService.findByEmail(principal.getName());
        return ok(orderService.repeatOrder(order, user));
    }

    @PreAuthorize("hasAuthority(T(com.kmadrugstore.utils.Constants)" +
            ".EMPLOYEE_ROLE_STRING)")
    @PutMapping("/order/change-status")
    public ResponseEntity<DetailedOrderDTO> changeOrderStatus(
            @RequestBody final OrderStatusDTO order) {

        return ok(orderService.changeStatus(order));
    }

    @PreAuthorize("hasAuthority(T(com.kmadrugstore.utils.Constants)" +
            ".EMPLOYEE_ROLE_STRING)")
    @GetMapping("/order-statuses")
    public ResponseEntity<List<OrderStatus>> getAllStatusesForOrders() {
        return ok(orderService.getAllStatuses());
    }

    @PreAuthorize("hasAuthority(T(com.kmadrugstore.utils.Constants)" +
            ".EMPLOYEE_ROLE_STRING)")
    @GetMapping("/all-orders/quantity")
    public ResponseEntity<Map<Object, Object>> getAllOrders(
            @RequestParam(value = "status", required = false)
            final String status) {
        Long result;
        if (status == null)
            result = orderService.quantityOfAllOrders();
        else
            result = orderService.quantityOfAllOrdersByStatus(status);
        Map<Object, Object> model = new HashMap<>();
        model.put("quantity", result);
        return ok(model);
    }

    @PreAuthorize("hasAuthority(T(com.kmadrugstore.utils.Constants)" +
            ".EMPLOYEE_ROLE_STRING)")
    @GetMapping("/orders/{id}")
    public ResponseEntity<DetailedOrderDTO> getOrderById(
            @PathVariable("id") final int id) {
        Order order = orderService.findOrderById(id);
        DetailedOrderDTO res = orderService.orderToDetailedOrderDTO(order);
        return ok(res);
    }

    @PreAuthorize("hasAuthority(T(com.kmadrugstore.utils.Constants)" +
            ".EMPLOYEE_ROLE_STRING)")
    @GetMapping("/all-orders")
    public ResponseEntity<Map<Object, Object>> getAllOrders(
            @RequestParam(value = "page") final int page,
            @RequestParam("page-size") final int pageSize,
            @RequestParam(value = "status", required = false)
            final String status) {
        Page<DetailedOrderDTO> result;
        if (status == null)
            result = orderService.findAllOrders(PageRequest.of(page, pageSize
                    , Sort.by("date").descending()));
        else
            result = orderService.findByStatus(PageRequest.of(page, pageSize,
                    Sort.by("date").descending()), Integer.parseInt(status));

        Map<Object, Object> model = new HashMap<>();
        model.put("orders", result.getContent());
        model.put("numPages", result.getTotalPages());
        return ok(model);
    }





}
