package com.Hirav.real_estate.entity;

import com.Hirav.real_estate.entity.enums.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
public class Booking {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "property_id", nullable = false)
    @JsonIgnoreProperties({"bookings"})
    private Property property;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnoreProperties({"properties", "bookings", "password"})
    private User user;

    @Column(nullable = false)
    private LocalDate bookingDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status;

    
    public Booking() {
		super();
	}
    
	public Booking(Property property, User user, LocalDate bookingDate, BookingStatus status) {
		super();
		this.property = property;
		this.user = user;
		this.bookingDate = bookingDate;
		this.status = status;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Property getProperty() {
        return property;
    }

    public void setProperty(Property property) {
        this.property = property;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public LocalDate getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(LocalDate bookingDate) {
        this.bookingDate = bookingDate;
    }

    public BookingStatus getStatus() {
        return status;
    }

    public void setStatus(BookingStatus status) {
        this.status = status;
    }

	@Override
	public String toString() {
		return "Booking [property=" + property + ", user=" + user + ", bookingDate=" + bookingDate + ", status="
				+ status + "]";
	}

}
