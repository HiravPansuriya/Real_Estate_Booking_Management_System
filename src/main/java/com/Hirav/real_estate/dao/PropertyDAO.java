package com.Hirav.real_estate.dao;

import com.Hirav.real_estate.entity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PropertyDAO extends JpaRepository<Property, Long> {
	
    boolean existsByTitleAndOwnerId(String title, Long ownerId);
    List<Property> findByTitleAndLocation(String title, String location);
}
