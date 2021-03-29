package com.kmadrugstore.repository;

import com.kmadrugstore.entity.Kit;
import com.kmadrugstore.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IKitRepository extends JpaRepository<Kit, Integer> {



}
