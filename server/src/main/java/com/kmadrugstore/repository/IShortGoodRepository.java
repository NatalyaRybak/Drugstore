package com.kmadrugstore.repository;

import com.kmadrugstore.entity.ShortGood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IShortGoodRepository extends JpaRepository<ShortGood,
        Integer> {

}
