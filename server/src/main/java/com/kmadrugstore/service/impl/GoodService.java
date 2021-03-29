package com.kmadrugstore.service.impl;

import com.kmadrugstore.dto.*;
import com.kmadrugstore.entity.*;
import com.kmadrugstore.exception.GoodNotFoundException;
import com.kmadrugstore.exception.GoodNotInStockException;
import com.kmadrugstore.repository.*;
import com.kmadrugstore.service.IGoodService;
import com.kmadrugstore.utils.GoodFilter;
import com.kmadrugstore.utils.GoodSpecification;
import com.kmadrugstore.utils.OrderStatusFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GoodService implements IGoodService {

    private final IGoodRepository goodRepository;
    private final IShortGoodRepository shortGoodRepository;
    private final ICategoryRepository categoryRepository;
    private final IActiveComponentRepository activeComponentRepository;
    private final IKitRepository kitRepository;
    private final IKitLineRepository kitLineRepository;

    @Override
    public Page<Good> findAllGoods(final GoodFilter filter,
                                   final Pageable pageable) {
        return goodRepository.findAll(
                GoodSpecification.satisfiesFilter(filter), pageable);
    }

    @Override
    public Good findGoodById(final int id) {
        return goodRepository.findById(id).orElseThrow(GoodNotFoundException::new);
    }

    @Override
    public ShortGood findShortGoodById(int id) {
        return shortGoodRepository.findById(id).orElseThrow(GoodNotFoundException::new);
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

    @Override
    public BigDecimal getPriceById(final int id) {
        return goodRepository.getPriceById(id).orElseThrow(GoodNotFoundException::new);
    }

    @Override
    public void increaseBy(final int goodId, final int n) {
        Good good = findGoodById(goodId);
        int avail = good.getNumAvailable();
        good.setNumAvailable((avail + n));
        goodRepository.save(good);
    }

    @Override
    public void decreaseBy(final int goodId, final int n) {
        Good good = findGoodById(goodId);
        int avail = good.getNumAvailable();
        if ((avail - n) < 0) {
            throw new GoodNotInStockException();
        }
        good.setNumAvailable((avail - n));
        goodRepository.save(good);
    }

    @Override
    public Integer getAvailableQuantity(final Good good) {
        return good.getNumAvailable();
    }


    @Override
    public List<KitLine> getGoodsFromKit(int id) {
        return kitLineRepository.findByKit_Id(id);
    }


    @Override
    public Page<KitDTO> findAllKits(final Pageable pageable){
        Page<Kit> kitsPage = kitRepository.findAll(pageable);
        return kitsPage.map(this::kitToKitDTO);
    }

    @Override
    public KitDTO kitToKitDTO(final Kit kit) {
        return doKitToKitDTO(kit,
                getGoodsFromKit(kit.getId()));
    }

    @Override
    public KitDTO doFindKitById(final int id)
    {
        Kit kit = findKitById(id);
        return kitToKitDTO(kit);
    }

    @Override
    public Kit findKitById(final int id) {
        return kitRepository.findById(id).orElseThrow(GoodNotFoundException::new);
    }

    @Override
    public List<OrderLineDTO> findAllGoodsInKits(final List<Integer> ids) {
        List<OrderLineDTO> futureOrderLines = new ArrayList<OrderLineDTO>();

        List<Kit> kits = new ArrayList<Kit>();
        for (int i=0; i<ids.size(); i++){

            List<KitLine> klList = getGoodsFromKit(ids.get(i));

            for(int j=0; j<klList.size(); j++){
                KitLine o = klList.get(j);
                futureOrderLines.add(OrderLineDTO.builder()
                        .goodId(o.getGood().getId())
                        .amount(o.getQuantity()).build()
                );
            }
        }



        return futureOrderLines;
    }

    /************** MAPPERS ***************/

    private KitDTO doKitToKitDTO(final Kit kit,  final List<KitLine> list) {
        List<KitLineDTO> newLines = new ArrayList<>();
        for (KitLine orderLine : list) {
            newLines.add(kitLineToKitLineDTO(orderLine));
        }

        return KitDTO.builder()
                .id(kit.getId())
                .description(kit.getDescription())
                .kitLines(newLines)
                .photo(kit.getPhoto())
                .price(kit.getPrice())
                .title(kit.getTitle())
                .build();
    }


    private KitLineDTO kitLineToKitLineDTO(KitLine line) {

        GoodDTO g = goodToGoodDTO(findGoodById(line.getGood().getId()));

        return KitLineDTO.builder()
                .good(g)
                .quantity(line.getQuantity())
                .build();
    }


    @Override
    public GoodDTO goodToGoodDTO(Good good) {
        return GoodDTO.builder()
                .id(good.getId())
                .dose(good.getDose())
                .form(good.getForm())
                .isPrescriptionNeeded(good.isPrescriptionNeeded())
                .name(good.getName())
                .numInPack(good.getNumInPack())
                .photo(good.getPhoto())
                .price(good.getPrice())
                .build();
    }

}
