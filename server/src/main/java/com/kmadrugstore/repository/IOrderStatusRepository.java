package com.kmadrugstore.repository;

import com.kmadrugstore.entity.Category;
import com.kmadrugstore.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IOrderStatusRepository extends JpaRepository<OrderStatus, Integer> {

}