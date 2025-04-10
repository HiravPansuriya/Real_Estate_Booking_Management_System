package com.Hirav.real_estate.service.impl;

import com.Hirav.real_estate.dao.UserDAO;
import com.Hirav.real_estate.entity.User;
import com.Hirav.real_estate.exception.DuplicateResourceException;
import com.Hirav.real_estate.exception.ResourceNotFoundException;
import com.Hirav.real_estate.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserDAO userDAO;

    public UserServiceImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public User saveUser(User user) {
        if (!findByUsername(user.getUsername()).isEmpty()) {
            throw new DuplicateResourceException("Username already exists: " + user.getUsername());
        }

        if (existsByEmail(user.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + user.getEmail());
        }

        return userDAO.save(user);
    }

    @Override
    public User updateUser(Long id, User updatedUser) {
        User existingUser = userDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        if (!existingUser.getUsername().equals(updatedUser.getUsername()) &&
        		!findByUsername(updatedUser.getUsername()).isEmpty()) {
            throw new DuplicateResourceException("Username already exists: " + updatedUser.getUsername());
        }

        if (!existingUser.getEmail().equals(updatedUser.getEmail()) &&
                existsByEmail(updatedUser.getEmail())) {
            throw new DuplicateResourceException("Email already exists: " + updatedUser.getEmail());
        }

        existingUser.setUsername(updatedUser.getUsername());
        existingUser.setEmail(updatedUser.getEmail());
        existingUser.setPassword(updatedUser.getPassword());
        existingUser.setRole(updatedUser.getRole());

        return userDAO.save(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        User user = userDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));
        userDAO.delete(user);
    }

    @Override
    public Optional<User> findUserById(Long id) {
        return userDAO.findById(id);
    }

//    @Override
//    public Optional<User> findUserByEmail(String email) {
//        return userDAO.findByEmail(email);
//    }
    
    @Override
    public Optional<User> findByUsername(String username) {
    	return userDAO.findByUsername(username);
    }
    
    @Override
    public List<User> getAllUsers() {
        return userDAO.findAll();
    }

//    @Override
//    public boolean existsByUsername(String username) {
//        return userDAO.existsByUsername(username);
//    }

    @Override
    public boolean existsByEmail(String email) {
        return userDAO.existsByEmail(email);
    }
}
