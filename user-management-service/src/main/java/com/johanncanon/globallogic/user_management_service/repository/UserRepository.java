package com.johanncanon.globallogic.user_management_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.johanncanon.globallogic.user_management_service.entity.User;

@Repository
public interface UserRepository extends JpaRepository< User, Long > {

    Optional<User> findByUsername( String username );
    Optional<User> findByEmail( String email );
    boolean existsByUsername( String username );
    boolean existsByEmail( String email );

}
