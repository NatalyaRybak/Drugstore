package com.kmadrugstore.service;

import com.kmadrugstore.dto.DetailedOrderDTO;
import com.kmadrugstore.dto.GoodDTO;
import com.kmadrugstore.dto.KitDTO;
import com.kmadrugstore.dto.OrderLineDTO;
import com.kmadrugstore.entity.*;
import com.kmadrugstore.utils.GoodFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface IGoodService {

    Page<Good> findAllGoods(final GoodFilter filter, final Pageable pageable);

    Good findGoodById(final int id);

    ShortGood findShortGoodById(final int id);

    List<String> findDistinctManufacturers();

    List<String> findDistinctCountries();

    List<Category> findCategoriesSortedByTitleAsc();

    List<ActiveComponent> findActiveComponentsSortedByNameAsc();

    BigDecimal findMinPrice();

    BigDecimal findMaxPrice();

    BigDecimal getPriceById(final int id);

    void increaseBy(int goodId, int n);

    void decreaseBy(int goodId, int n);

    Integer getAvailableQuantity(final Good good);

    Page<KitDTO> findAllKits(final Pageable pageable);

    KitDTO kitToKitDTO(final Kit kit);

    List<KitLine> getGoodsFromKit(int id);

    GoodDTO goodToGoodDTO(Good good);

    Kit findKitById(final int id);

    KitDTO doFindKitById(final int id);

    List<OrderLineDTO> findAllGoodsInKits(final List<Integer> ids);
}
