package com.learning.nk.repository;

import com.learning.nk.model.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<CustomUser, Long> {
    Optional<CustomUser> findByUsername(String username);
    CustomUser getByUsername(String username);
}
