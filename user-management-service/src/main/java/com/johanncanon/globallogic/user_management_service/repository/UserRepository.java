package com.johanncanon.globallogic.user_management_service.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.johanncanon.globallogic.user_management_service.entity.User;

@Repository
public interface UserRepository extends JpaRepository< User, Long > {

    Optional<User> findByName( String name );
    Optional<User> findById( String email );
    boolean existsByName( String name );
    boolean existsByEmail( String email );
    boolean existsById( String id );
    
}
