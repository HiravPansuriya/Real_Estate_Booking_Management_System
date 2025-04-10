package com.Hirav.real_estate.service;

import com.Hirav.real_estate.entity.Booking;
import com.Hirav.real_estate.entity.enums.BookingStatus;
import java.util.List;
import java.util.Optional;

public interface BookingService {
	
    Booking saveBooking(String loggedInUsername, Booking booking);
    Booking updateBooking(Long id, Booking booking);
    public Booking updateBookingStatus(Long bookingId, Long ownerId, BookingStatus status);
    void deleteBooking(Long id);
    Optional<Booking> findBookingById(Long id);
//    List<Booking> getAllBookings();
    List<Booking> getBookingsByOwnerId(Long ownerId);
    List<Booking> getBookingsByUserId(Long userId);

}
