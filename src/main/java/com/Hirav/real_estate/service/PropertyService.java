package com.Hirav.real_estate.service;

import com.Hirav.real_estate.entity.Property;
import java.util.List;
import java.util.Optional;

public interface PropertyService {
	
    Property saveProperty(Property property);
    Property updateProperty(Long id, Property property);
    void deleteProperty(Long id);
    Optional<Property> findPropertyById(Long id);
    List<Property> getAllProperties();

}
