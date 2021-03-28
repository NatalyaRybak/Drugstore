package com.kmadrugstore.repository;

import com.kmadrugstore.entity.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface IGoodRepository extends JpaRepository<Good, Integer>,
        JpaSpecificationExecutor<Good> {

    @Query("select distinct manufacturer from Good order by manufacturer asc")
    List<String> findDistinctManufacturers();

    @Query("select distinct country from Good order by country asc")
    List<String> findDistinctCountries();

    @Query("select min(price) from Good")
    BigDecimal findMinPrice();

    @Query("select max(price) from Good")
    BigDecimal findMaxPrice();
}
