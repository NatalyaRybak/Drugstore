package com.kmadrugstore.controller;

import com.kmadrugstore.entity.ActiveComponent;
import com.kmadrugstore.entity.Category;
import com.kmadrugstore.entity.Good;
import com.kmadrugstore.entity.ShortGood;
import com.kmadrugstore.service.IGoodService;
import com.kmadrugstore.utils.GoodFilter;
import com.kmadrugstore.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/api/v1")
public class GoodController {

    private IGoodService goodService;

    @Autowired
    public GoodController(final IGoodService goodService) {
        this.goodService = goodService;
    }

    @GetMapping("/goods/{id}")
    public ResponseEntity<Good> getGoodById(@PathVariable("id") final int id) {
        Optional<Good> good = goodService.findGoodById(id);
        if (good.isPresent()) {
            return ok(good.get());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/goods-short/{id}")
    public ResponseEntity<ShortGood> getShortGoodById(@PathVariable("id") final int id) {
        Optional<ShortGood> good = goodService.findShortGoodById(id);
        if (good.isPresent()) {
            return ok(good.get());
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/goods")
    public ResponseEntity<Map<Object, Object>> getGoods(
            @RequestParam(value = "search", required = false) final String search,
            @RequestParam(value = "price-min", required = false) final BigDecimal priceMin,
            @RequestParam(value = "price-max", required = false) final BigDecimal priceMax,
            @RequestParam(value = "manufacturer", required = false) final String manufacturer,
            @RequestParam(value = "country", required = false) final String country,
            @RequestParam(value = "category-id", required = false) final Integer categoryId,
            @RequestParam(value = "active-component-id", required = false) final Integer activeComponentId,
            @RequestParam("page") final int page,
            @RequestParam("page-size") final int pageSize) {
        Page<Good> result = goodService.findAllGoods(
                new GoodFilter(search, priceMin, priceMax, manufacturer,
                        country, categoryId, activeComponentId),
                PageRequest.of(page, pageSize));
        Map<Object, Object> model = new HashMap<>();
        model.put("goods", Utils.toShortGoods(result.getContent()));
        model.put("numPages", result.getTotalPages());
        return ok(model);
    }

    @GetMapping("/manufacturers")
    public ResponseEntity<List<String>> getManufacturers() {
        return ok(goodService.findDistinctManufacturers());
    }

    @GetMapping("/countries")
    public ResponseEntity<List<String>> getCountries() {
        return ok(goodService.findDistinctCountries());
    }

    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {
        return ok(goodService.findCategoriesSortedByTitleAsc());
    }

    @GetMapping("/active-components")
    public ResponseEntity<List<ActiveComponent>> getActiveComponents() {
        return ok(goodService.findActiveComponentsSortedByNameAsc());
    }

    @GetMapping("/price-min")
    public ResponseEntity<BigDecimal> getMinPrice() {
        return ok(goodService.findMinPrice());
    }

    @GetMapping("/price-max")
    public ResponseEntity<BigDecimal> getMaxPrice() {
        return ok(goodService.findMaxPrice());
    }

}
