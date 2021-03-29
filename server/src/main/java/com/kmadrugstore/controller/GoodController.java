package com.kmadrugstore.controller;

import com.kmadrugstore.dto.KitDTO;
import com.kmadrugstore.dto.OrderLineDTO;
import com.kmadrugstore.entity.ActiveComponent;
import com.kmadrugstore.entity.Category;
import com.kmadrugstore.entity.Good;
import com.kmadrugstore.entity.ShortGood;
import com.kmadrugstore.service.IGoodService;
import com.kmadrugstore.utils.GoodFilter;
import com.kmadrugstore.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class GoodController {

    private final IGoodService goodService;

    @PreAuthorize("permitAll()")
    @GetMapping("/goods/{id}")
    public ResponseEntity<Good> getGoodById(@PathVariable("id") final int id) {
        return ok(goodService.findGoodById(id));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/goods-short/{id}")
    public ResponseEntity<ShortGood> getShortGoodById(@PathVariable("id") final int id) {
        return ok(goodService.findShortGoodById(id));
    }

    @PreAuthorize("permitAll()")
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

    @PreAuthorize("permitAll()")
    @GetMapping("/manufacturers")
    public ResponseEntity<List<String>> getManufacturers() {
        return ok(goodService.findDistinctManufacturers());
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/countries")
    public ResponseEntity<List<String>> getCountries() {
        return ok(goodService.findDistinctCountries());
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/categories")
    public ResponseEntity<List<Category>> getCategories() {

        return ok(goodService.findCategoriesSortedByTitleAsc());
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/active-components")
    public ResponseEntity<List<ActiveComponent>> getActiveComponents() {
        return ok(goodService.findActiveComponentsSortedByNameAsc());
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/price-min")
    public ResponseEntity<BigDecimal> getMinPrice() {
        return ok(goodService.findMinPrice());
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/price-max")
    public ResponseEntity<BigDecimal> getMaxPrice() {
        return ok(goodService.findMaxPrice());
    }


    @PreAuthorize("permitAll()")
    @GetMapping("/kits")
    public ResponseEntity<Map<Object, Object>> getAllKits(
            @RequestParam(value = "page") final int page,
            @RequestParam("page-size") final int pageSize) {

        Page<KitDTO> result;
        result = goodService.findAllKits(PageRequest.of(page, pageSize));

        Map<Object, Object> model = new HashMap<>();
        model.put("kits", result.getContent());
        model.put("numPages", result.getTotalPages());
        return ok(model);
    }


    @PreAuthorize("permitAll()")
    @PostMapping("/kits-to-goods")
    public ResponseEntity<Map<Object, Object>> saveUserOrder(
            @RequestBody final List<Integer> ids) {

        List<OrderLineDTO> result;
        result = goodService.findAllGoodsInKits(ids);

        Map<Object, Object> model = new HashMap<>();
        model.put("goodsInKit", result);
        return ok(model);
    }

   @PreAuthorize("permitAll()")
    @GetMapping("/kits/{id}")
    public ResponseEntity<KitDTO> getKitById(@PathVariable("id") final int id) {
        return ok(goodService.doFindKitById(id));
    }

}
