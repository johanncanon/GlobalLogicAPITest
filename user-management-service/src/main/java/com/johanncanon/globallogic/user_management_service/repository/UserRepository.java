package com.johanncanon.globallogic.user_management_service.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.johanncanon.globallogic.user_management_service.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByName(String name);

    boolean existsByName(String name);

    boolean existsByEmail(String email);

}
