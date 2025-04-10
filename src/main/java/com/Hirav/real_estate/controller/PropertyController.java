package com.Hirav.real_estate.controller;

import com.Hirav.real_estate.entity.Property;
import com.Hirav.real_estate.entity.User;
import com.Hirav.real_estate.exception.ResourceNotFoundException;
import com.Hirav.real_estate.service.PropertyService;
import com.Hirav.real_estate.service.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/properties")
public class PropertyController {

    private final PropertyService propertyService;
    private final UserService userService;

    public PropertyController(PropertyService propertyService, UserService userService) {
        this.propertyService = propertyService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Property> createProperty(@RequestBody Property property, Authentication authentication) {
        String loggedInUsername = authentication.getName();

        User owner = userService.findByUsername(loggedInUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + loggedInUsername));

        property.setOwner(owner);

        Property savedProperty = propertyService.saveProperty(property);
        return new ResponseEntity<>(savedProperty, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateProperty(@PathVariable Long id, @RequestBody Property property, Authentication authentication) {
        String loggedInUsername = authentication.getName();

        Property existingProperty = propertyService.findPropertyById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + id));

        if (!existingProperty.getOwner().getUsername().equals(loggedInUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own property.");
        }

        existingProperty.setTitle(property.getTitle());
        existingProperty.setLocation(property.getLocation());
        existingProperty.setPrice(property.getPrice());
        existingProperty.setStatus(property.getStatus());

        propertyService.updateProperty(id, existingProperty);
        return ResponseEntity.ok(existingProperty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProperty(@PathVariable Long id, Authentication authentication) {
        String loggedInUsername = authentication.getName();

        Property existingProperty = propertyService.findPropertyById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + id));

        if (!existingProperty.getOwner().getUsername().equals(loggedInUsername)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own property.");
        }

        propertyService.deleteProperty(id);
        return ResponseEntity.ok("Property deleted successfully!");
    }

    @GetMapping("/{id}")
    public ResponseEntity<Property> getPropertyById(@PathVariable Long id) {
        Property property = propertyService.findPropertyById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + id));
        return ResponseEntity.ok(property);
    }

    @GetMapping
    public ResponseEntity<List<Property>> getAllProperties() {
        return ResponseEntity.ok(propertyService.getAllProperties());
    }
}
