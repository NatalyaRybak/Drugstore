package com.kmadrugstore.repository;

import com.kmadrugstore.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IOrderRepository extends JpaRepository<Order, Integer> {

    Page<Order> findByStatus_Id(Integer status, Pageable pageable);

    Page<Order> findByUser_Id(Integer userId, Pageable pageable);

    Long countByStatus_Id(int status);

    List<Order> findAllByExpirationDateBeforeAndStatus_IdIn(
            LocalDateTime timestamp, List<Integer> statusIds);

}
