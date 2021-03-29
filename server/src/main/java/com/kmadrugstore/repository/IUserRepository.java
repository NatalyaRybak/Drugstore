package com.kmadrugstore.repository;

import com.kmadrugstore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IUserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByNameAndSurnameAndEmail(final String name,
                                            final String surname,
                                            final String email);

    Optional<User> findByEmail(final String email);

    Optional<User> findById(final int id);
}
