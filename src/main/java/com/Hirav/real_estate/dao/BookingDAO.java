package com.Hirav.real_estate.dao;

import com.Hirav.real_estate.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookingDAO extends JpaRepository<Booking, Long> {
	
    @Query("SELECT b FROM Booking b WHERE b.property.owner.id = :ownerId")
    List<Booking> findByPropertyOwnerId(@Param("ownerId") Long ownerId);
    
    @Query("SELECT b FROM Booking b WHERE b.user.id = :userId")
    List<Booking> findByUserId(@Param("userId") Long userId);
    
    boolean existsByUserIdAndPropertyId(Long userId, Long propertyId);
}
