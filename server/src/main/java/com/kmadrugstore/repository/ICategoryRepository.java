package com.kmadrugstore.repository;

import com.kmadrugstore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICategoryRepository extends JpaRepository<Category, Integer> {

    List<Category> findAllByOrderByTitleAsc();
}
