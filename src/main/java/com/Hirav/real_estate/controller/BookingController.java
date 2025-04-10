package com.Hirav.real_estate.controller;

import com.Hirav.real_estate.entity.Booking;
import com.Hirav.real_estate.entity.User;
import com.Hirav.real_estate.entity.enums.BookingStatus;
import com.Hirav.real_estate.exception.ResourceNotFoundException;
import com.Hirav.real_estate.service.BookingService;
import com.Hirav.real_estate.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/bookings")
public class BookingController {

    private final BookingService bookingService;
    private final UserService userService;

    public BookingController(BookingService bookingService, UserService userService) {
        this.bookingService = bookingService;
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<Booking> createBooking(@RequestBody Booking booking, Authentication authentication) {
        String loggedInUsername = authentication.getName();
        return ResponseEntity.ok(bookingService.saveBooking(loggedInUsername, booking));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(@PathVariable Long id, @RequestBody Booking updatedBooking, Authentication authentication) {
        String loggedInUsername = authentication.getName();
        User loggedInUser = userService.findByUsername(loggedInUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + loggedInUsername));

        Booking existingBooking = bookingService.findBookingById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!existingBooking.getUser().getId().equals(loggedInUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only update your own booking date.");
        }

        existingBooking.setBookingDate(updatedBooking.getBookingDate());
        bookingService.updateBooking(id, existingBooking);
        return ResponseEntity.ok(existingBooking);
    }

    @PutMapping("/{bookingId}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long bookingId, @RequestBody Map<String, String> requestBody, Authentication authentication) {
        String loggedInUsername = authentication.getName();
        User loggedInUser = userService.findByUsername(loggedInUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + loggedInUsername));

        Booking booking = bookingService.findBookingById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        if (!booking.getProperty().getOwner().getId().equals(loggedInUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Only the property owner can update booking status.");
        }

        String statusValue = requestBody.get("status");
        try {
            BookingStatus status = BookingStatus.valueOf(statusValue.toUpperCase());
            booking.setStatus(status);
        } catch (IllegalArgumentException | NullPointerException e) {
            return ResponseEntity.badRequest().body("Invalid booking status.");
        }

        bookingService.updateBooking(bookingId, booking);
        return ResponseEntity.ok(booking);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBooking(@PathVariable Long id, Authentication authentication) {
        String loggedInUsername = authentication.getName();
        User loggedInUser = userService.findByUsername(loggedInUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + loggedInUsername));

        Booking booking = bookingService.findBookingById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found"));

        // Only the buyer can delete their booking
        if (!booking.getUser().getId().equals(loggedInUser.getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You can only delete your own booking.");
        }

        bookingService.deleteBooking(id);
        return ResponseEntity.ok("Booking deleted successfully!");
    }

    @GetMapping("/buyer")
    public ResponseEntity<List<Booking>> getBookingsForBuyer(Authentication authentication) {
        String loggedInUsername = authentication.getName();
        User loggedInUser = userService.findByUsername(loggedInUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + loggedInUsername));

        return ResponseEntity.ok(bookingService.getBookingsByUserId(loggedInUser.getId()));
    }

    @GetMapping("/owner")
    public ResponseEntity<List<Booking>> getBookingsForOwner(Authentication authentication) {
        String loggedInUsername = authentication.getName();
        User loggedInUser = userService.findByUsername(loggedInUsername)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + loggedInUsername));

        return ResponseEntity.ok(bookingService.getBookingsByOwnerId(loggedInUser.getId()));
    }
}
