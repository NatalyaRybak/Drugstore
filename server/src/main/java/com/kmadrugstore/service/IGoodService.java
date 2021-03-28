package com.kmadrugstore.service;

import com.kmadrugstore.entity.ActiveComponent;
import com.kmadrugstore.entity.Category;
import com.kmadrugstore.entity.Good;
import com.kmadrugstore.entity.ShortGood;
import com.kmadrugstore.utils.GoodFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface IGoodService {

    Page<Good> findAllGoods(final GoodFilter filter, final Pageable pageable);

    Optional<Good> findGoodById(final int id);

    Optional<ShortGood> findShortGoodById(final int id);

    List<String> findDistinctManufacturers();

    List<String> findDistinctCountries();

    List<Category> findCategoriesSortedByTitleAsc();

    List<ActiveComponent> findActiveComponentsSortedByNameAsc();

    BigDecimal findMinPrice();

    BigDecimal findMaxPrice();
}
