package com.Hirav.real_estate.dao;

import com.Hirav.real_estate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<User, Long> {
	
//    Optional<User> findByEmail(String email);
//    boolean existsByUsername(String username);
    boolean existsByEmail(String email); 
    Optional<User> findByUsername(String username);
}
