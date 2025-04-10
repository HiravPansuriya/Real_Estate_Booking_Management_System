package com.Hirav.real_estate.controller;

import com.Hirav.real_estate.dto.UserDTOReg;
import com.Hirav.real_estate.entity.User;
import com.Hirav.real_estate.exception.ResourceNotFoundException;
import com.Hirav.real_estate.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody UserDTOReg userDTO) {
    	System.out.print(userDTO);
    	User user=new User(userDTO.getUsername(),userDTO.getPassword(),userDTO.getEmail(),userDTO.getRole());
        user.setPassword(new BCryptPasswordEncoder().encode(userDTO.getPassword()));
        User savedUser = userService.saveUser(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody User user, Authentication authentication) {
        String loggedInUsername = authentication.getName();

        User existingUser = userService.findUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        if (!existingUser.getUsername().equals(loggedInUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own account.");
        }

        existingUser.setEmail(user.getEmail());

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existingUser.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        }

        userService.updateUser(id, existingUser);
        return ResponseEntity.ok(existingUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id, Authentication authentication) {

        userService.deleteUser(id);
        return ResponseEntity.ok("User deleted successfully!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id, Authentication authentication) {
    	
        String loggedInUsername = authentication.getName();

        User user = userService.findUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + id));

        if (!user.getUsername().equals(loggedInUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only view your own account.");
        }

        return ResponseEntity.ok(user);
    }


    @GetMapping
    public ResponseEntity<List<User>> getAllUsers(Authentication authentication) {
        if (!authentication.getAuthorities().stream().anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }

        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
}
