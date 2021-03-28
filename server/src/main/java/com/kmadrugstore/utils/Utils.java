package com.kmadrugstore.utils;

import com.kmadrugstore.entity.Good;
import com.kmadrugstore.entity.ShortGood;

import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static ShortGood toShortGood(final Good good) {
        return new ShortGood(good.getId(), good.getName(), good.getPrice(),
                good.getNumAvailable(), good.getNumInPack(), good.getDose(),
                good.getPhoto());
    }

    public static List<ShortGood> toShortGoods(final List<Good> goods) {
        List<ShortGood> res = new ArrayList<>(goods.size());
        for (Good good : goods) {
            res.add(toShortGood(good));
        }
        return res;
    }

}
