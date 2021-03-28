package com.kmadrugstore.utils;

import com.kmadrugstore.entity.ActiveComponent;
import com.kmadrugstore.entity.Good;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.math.BigDecimal;
import java.util.Collection;

public class GoodSpecification {

    public static Specification<Good> nameContains(final String search) {
        return (r, cq, cb) -> cb
                .like(cb.lower(r.get("name")), "%" + search.toLowerCase() +
                        "%");
    }

    public static Specification<Good> priceInRange(final BigDecimal min,
                                                   final BigDecimal max) {
        return (r, cq, cb) -> cb.between(r.get("price"), min, max);
    }

    public static Specification<Good> priceAbove(final BigDecimal price) {
        return (r, cq, cb) -> cb.ge(r.get("price"), price);
    }

    public static Specification<Good> priceBelow(final BigDecimal price) {
        return (r, cq, cb) -> cb.le(r.get("price"), price);
    }

    public static Specification<Good> manufacturerIs(final String manufacturer) {
        return (r, cq, cb) -> cb.equal(r.get("manufacturer"), manufacturer);
    }

    public static Specification<Good> countryIs(final String country) {
        return (r, cq, cb) -> cb.equal(r.get("country"), country);
    }

    public static Specification<Good> categoryIs(final int id) {
        return (r, cq, cb) -> cb.equal(r.get("category").get("id"), id);
    }

    public static Specification<Good> activeComponentPresent(final int id) {
        return (r, cq, cb) -> {
            cq.distinct(true);
            Subquery<ActiveComponent> subQuery =
                    cq.subquery(ActiveComponent.class);
            Root<ActiveComponent> component =
                    subQuery.from(ActiveComponent.class);
            Expression<Collection<ActiveComponent>> components = r.get(
                    "activeComponents");
            subQuery.select(component);
            subQuery.where(cb.equal(component.get("id"), id),
                    cb.isMember(component, components));
            return cb.exists(subQuery);
        };
    }

    public static Specification<Good> satisfiesFilter(final GoodFilter filter) {
        Specification<Good> spec = Specification.where(null);
        if (filter.getSearch() != null) {
            spec = spec.and(nameContains(filter.getSearch()));
        }
        if (filter.getPriceMin() != null) {
            spec = spec.and(priceAbove(filter.getPriceMin()));
        }
        if (filter.getPriceMax() != null) {
            spec = spec.and(priceBelow(filter.getPriceMax()));
        }
        if (filter.getManufacturer() != null) {
            spec = spec.and(manufacturerIs(filter.getManufacturer()));
        }
        if (filter.getCountry() != null) {
            spec = spec.and(countryIs(filter.getCountry()));
        }
        if (filter.getCategoryId() != null) {
            spec = spec.and(categoryIs(filter.getCategoryId()));
        }
        if (filter.getActiveComponentId() != null) {
            spec = spec.and(activeComponentPresent(filter.getActiveComponentId()));
        }
        return spec;
    }

}
