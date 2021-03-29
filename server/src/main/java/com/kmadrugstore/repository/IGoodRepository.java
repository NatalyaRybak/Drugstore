package com.kmadrugstore.repository;

import com.kmadrugstore.entity.Good;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface IGoodRepository extends JpaRepository<Good, Integer>,
        JpaSpecificationExecutor<Good> {

    @Query(value = "select distinct manufacturer from Good order by " +
            "manufacturer asc", nativeQuery = true)
    List<String> findDistinctManufacturers();

    @Query(value = "select distinct country from Good order by country asc",
            nativeQuery = true)
    List<String> findDistinctCountries();

    @Query(value = "select min(price) from Good", nativeQuery = true)
    BigDecimal findMinPrice();

    @Query(value = "select max(price) from Good", nativeQuery = true)
    BigDecimal findMaxPrice();

    @Query(value = "select g.price from Good g where g.id = :id",
            nativeQuery = true)
    Optional<BigDecimal> getPriceById(@Param("id") final int id);
}
