package com.kmadrugstore.repository;

import com.kmadrugstore.entity.ActiveComponent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IActiveComponentRepository extends JpaRepository<ActiveComponent, Integer> {

    List<ActiveComponent> findAllByOrderByNameAsc();
}
