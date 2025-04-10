package com.Hirav.real_estate.service.impl;

import com.Hirav.real_estate.dao.BookingDAO;
import com.Hirav.real_estate.dao.PropertyDAO;
import com.Hirav.real_estate.dao.UserDAO;
import com.Hirav.real_estate.entity.Booking;
import com.Hirav.real_estate.entity.Property;
import com.Hirav.real_estate.entity.User;
import com.Hirav.real_estate.entity.enums.Role;
import com.Hirav.real_estate.entity.enums.BookingStatus;
import com.Hirav.real_estate.entity.enums.PropertyStatus;
import com.Hirav.real_estate.exception.AccessDeniedException;
import com.Hirav.real_estate.exception.ResourceNotFoundException;
import com.Hirav.real_estate.service.BookingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingDAO bookingDAO;
    private final UserDAO userDAO;
    private final PropertyDAO propertyDAO;

    public BookingServiceImpl(BookingDAO bookingDAO, UserDAO userDAO, PropertyDAO propertyDAO) {
        this.bookingDAO = bookingDAO;
        this.userDAO = userDAO;
        this.propertyDAO = propertyDAO;
    }

    @Override
    public Booking saveBooking(String username, Booking booking) {
        User user = userDAO.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + username));

        if (!user.getRole().equals(Role.BUYER)) {
            throw new AccessDeniedException("Only buyers can book an appointment.");
        }

        if (booking.getProperty() == null || booking.getProperty().getId() == null) {
            throw new ResourceNotFoundException("Property ID is required for booking.");
        }

        Property property = propertyDAO.findById(booking.getProperty().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with ID: " + booking.getProperty().getId()));

        if (property.getStatus() != PropertyStatus.AVAILABLE) {
            throw new IllegalStateException("This property is not available for booking.");
        }

        if (property.getOwner().getId().equals(user.getId())) {
            throw new AccessDeniedException("You cannot book your own property.");
        }

        boolean alreadyBooked = bookingDAO.existsByUserIdAndPropertyId(user.getId(), property.getId());
        if (alreadyBooked) {
            throw new IllegalStateException("You have already booked an appointment for this property.");
        }

        LocalDate today = LocalDate.now();
        LocalDate bookingDate = booking.getBookingDate();

        if (bookingDate == null || !bookingDate.isAfter(today)) {
            throw new IllegalStateException("Booking date must be at least tomorrow.");
        }

        booking.setUser(user);
        booking.setProperty(property);
        booking.setStatus(BookingStatus.PENDING);
        return bookingDAO.save(booking);
    }


    @Override
    public Booking updateBooking(Long bookingId, Booking updatedBooking) {
        Booking existingBooking = bookingDAO.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        LocalDate today = LocalDate.now();
        LocalDate newBookingDate = updatedBooking.getBookingDate();

        if (newBookingDate == null || !newBookingDate.isAfter(today)) {
            throw new IllegalStateException("Booking date must be at least tomorrow.");
        }

        existingBooking.setBookingDate(newBookingDate);
        return bookingDAO.save(existingBooking);
    }

    @Override
    public Booking updateBookingStatus(Long bookingId, Long ownerId, BookingStatus status) {
        Booking existingBooking = bookingDAO.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + bookingId));

        if (!existingBooking.getProperty().getOwner().getId().equals(ownerId)) {
            throw new AccessDeniedException("Only the property owner can update booking status.");
        }

        if (existingBooking.getStatus() == status) {
            throw new IllegalStateException("Booking is already in status: " + status);
        }

        if (existingBooking.getStatus() == BookingStatus.CANCELLED) {
            throw new IllegalStateException("Cannot update a cancelled booking.");
        }

        existingBooking.setStatus(status);
        return bookingDAO.save(existingBooking);
    }

    @Override
    public void deleteBooking(Long id) {
        bookingDAO.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with ID: " + id));
        bookingDAO.deleteById(id);
    }

    @Override
    public Optional<Booking> findBookingById(Long id) {
        return bookingDAO.findById(id);
    }

//    @Override
//    public List<Booking> getAllBookings() {
//        return bookingDAO.findAll();
//    }

    @Override
    public List<Booking> getBookingsByOwnerId(Long ownerId) {
        return bookingDAO.findByPropertyOwnerId(ownerId);
    }

    @Override
    public List<Booking> getBookingsByUserId(Long userId) {
        return bookingDAO.findByUserId(userId);
    }
}
