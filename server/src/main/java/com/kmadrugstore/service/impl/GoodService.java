package com.kmadrugstore.service.impl;

import com.kmadrugstore.entity.ActiveComponent;
import com.kmadrugstore.entity.Category;
import com.kmadrugstore.entity.Good;
import com.kmadrugstore.entity.ShortGood;
import com.kmadrugstore.repository.IActiveComponentRepository;
import com.kmadrugstore.repository.ICategoryRepository;
import com.kmadrugstore.repository.IGoodRepository;
import com.kmadrugstore.repository.IShortGoodRepository;
import com.kmadrugstore.service.IGoodService;
import com.kmadrugstore.utils.GoodFilter;
import com.kmadrugstore.utils.GoodSpecification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class GoodService implements IGoodService {

    private IGoodRepository goodRepository;
    private IShortGoodRepository shortGoodRepository;
    private ICategoryRepository categoryRepository;
    private IActiveComponentRepository activeComponentRepository;

    @Autowired
    public GoodService(final IGoodRepository goodRepository,
                       final IShortGoodRepository shortGoodRepository,
                       final ICategoryRepository categoryRepository,
                       final IActiveComponentRepository activeComponentRepository) {
        this.goodRepository = goodRepository;
        this.shortGoodRepository = shortGoodRepository;
        this.categoryRepository = categoryRepository;
        this.activeComponentRepository = activeComponentRepository;
    }

    @Override
    public Page<Good> findAllGoods(final GoodFilter filter,
                                   final Pageable pageable) {
        return goodRepository.findAll(
                GoodSpecification.satisfiesFilter(filter), pageable);
    }

    @Override
    public Optional<Good> findGoodById(final int id) {
        return goodRepository.findById(id);
    }

    @Override
    public Optional<ShortGood> findShortGoodById(int id) {
        return shortGoodRepository.findById(id);
    }

    @Override
    public List<String> findDistinctManufacturers() {
        return goodRepository.findDistinctManufacturers();
    }

    @Override
    public List<String> findDistinctCountries() {
        return goodRepository.findDistinctCountries();
    }

    @Override
    public List<Category> findCategoriesSortedByTitleAsc() {
        return categoryRepository.findAllByOrderByTitleAsc();
    }

    @Override
    public List<ActiveComponent> findActiveComponentsSortedByNameAsc() {
        return activeComponentRepository.findAllByOrderByNameAsc();
    }

    @Override
    public BigDecimal findMinPrice() {
        return goodRepository.findMinPrice();
    }

    @Override
    public BigDecimal findMaxPrice() {
        return goodRepository.findMaxPrice();
    }
}
