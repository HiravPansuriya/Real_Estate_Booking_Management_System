package com.Hirav.real_estate.service.impl;

import com.Hirav.real_estate.dao.PropertyDAO;
import com.Hirav.real_estate.entity.Property;
import com.Hirav.real_estate.entity.User;
import com.Hirav.real_estate.entity.enums.Role;
import com.Hirav.real_estate.exception.ResourceAlreadyExistsException;
import com.Hirav.real_estate.exception.ResourceNotFoundException;
import com.Hirav.real_estate.exception.UnauthorizedException;
import com.Hirav.real_estate.service.PropertyService;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.Hirav.real_estate.dao.UserDAO;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class PropertyServiceImpl implements PropertyService {

    private final PropertyDAO propertyDAO;
    private final UserDAO userDAO;

    public PropertyServiceImpl(PropertyDAO propertyDAO, UserDAO userDAO) {
        this.propertyDAO = propertyDAO;
        this.userDAO = userDAO;
    }

    @Override
    public Property saveProperty(Property property) {
        User owner = userDAO.findById(property.getOwner().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + property.getOwner().getId()));

        if (!owner.getRole().equals(Role.SELLER)) {
            throw new UnauthorizedException("Only SELLERS can add properties.");
        }
        
        boolean exists = propertyDAO.existsByTitleAndOwnerId(property.getTitle(), owner.getId());
        if (exists) {
            throw new IllegalStateException("This property already exists for the owner.");
        }

        List<Property> existingProperties = propertyDAO.findByTitleAndLocation(property.getTitle(), property.getLocation());
        for (Property existingProperty : existingProperties) {
            if (!existingProperty.getOwner().getId().equals(owner.getId())) {
                throw new ResourceAlreadyExistsException("This property is already owned by another user.");
            }
        }

        property.setOwner(owner);

        return propertyDAO.save(property);
    }

    @Override
    public Property updateProperty(Long id, Property updatedProperty) {
        Property existingProperty = propertyDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + id));

        if (!existingProperty.getOwner().getId().equals(updatedProperty.getOwner().getId())) {
            throw new IllegalStateException("You are not the owner of this property.");
        }

        existingProperty.setTitle(updatedProperty.getTitle());
        existingProperty.setLocation(updatedProperty.getLocation());
        existingProperty.setPrice(updatedProperty.getPrice());
        existingProperty.setType(updatedProperty.getType());
        existingProperty.setStatus(updatedProperty.getStatus());

        return propertyDAO.save(existingProperty);
    }

    @Override
    public void deleteProperty(Long id) {
        Property property = propertyDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + id));

        
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String loggedInUsername = authentication.getName();

        User owner = property.getOwner();

        if (owner == null || !owner.getUsername().equals(loggedInUsername)) {
            throw new UnauthorizedException("You can only delete your own property.");
        }
        
        
        propertyDAO.delete(property);
    }

    @Override
    public Optional<Property> findPropertyById(Long id) {
        return propertyDAO.findById(id);
    }

    @Override
    public List<Property> getAllProperties() {
        return propertyDAO.findAll();
    }
    
}
