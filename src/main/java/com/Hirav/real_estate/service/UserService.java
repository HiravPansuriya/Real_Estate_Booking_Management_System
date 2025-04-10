package com.Hirav.real_estate.service;

import com.Hirav.real_estate.entity.User;
import java.util.List;
import java.util.Optional;

public interface UserService {
	
    User saveUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    Optional<User> findUserById(Long id);
//    Optional<User> findUserByEmail(String email);
    List<User> getAllUsers();
//    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
	Optional<User> findByUsername(String Username);

}
