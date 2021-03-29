package com.kmadrugstore.repository;

import com.kmadrugstore.entity.Order;
import com.kmadrugstore.entity.OrderLine;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IOrderLineRepository extends JpaRepository<OrderLine, Integer> {


    List<OrderLine> findByOrder_Id(Integer orderId);

}
