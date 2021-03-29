package com.kmadrugstore.repository;

import com.kmadrugstore.entity.Kit;
import com.kmadrugstore.entity.KitLine;
import com.kmadrugstore.entity.OrderLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IKitLineRepository extends JpaRepository<KitLine, Integer> {

    List<KitLine> findByKit_Id(Integer kitId);

}
