package com.satheeshtech.CABSpringBootRest.service;

import com.satheeshtech.CABSpringBootRest.model.CabBooking;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
public class CabBookingService {
    private final List<CabBooking> cabBookings = new ArrayList<>();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

    // Simulate cab data
    public CabBookingService() {
        cabBookings.add(new CabBooking(1L, "CAB-123", "AVAILABLE"));
        cabBookings.add(new CabBooking(2L, "CAB-456", "AVAILABLE"));
        cabBookings.add(new CabBooking(3L, "CAB-789", "AVAILABLE"));
        cabBookings.add(new CabBooking(4L, "CAB-790", "AVAILABLE"));
    }

    public List<CabBooking> getAllCabs() {
        return new ArrayList<>(cabBookings); // Return all cabs, including booked ones
    }

    public Optional<CabBooking> bookCab(Long cabId, String customerName) {
        Optional<CabBooking> cabBooking = cabBookings.stream()
                .filter(cab -> cab.getId().equals(cabId))
                .findFirst();

        if (cabBooking.isPresent() && "AVAILABLE".equalsIgnoreCase(cabBooking.get().getStatus())) {
            cabBooking.get().setCustomerName(customerName);
            cabBooking.get().setStatus("BOOKED");
            cabBooking.get().setBookingTime(LocalDateTime.now().format(dateTimeFormatter));
            return cabBooking;
        }
        return Optional.empty();
    }

    public Optional<CabBooking> cancelBooking(Long cabId) {
        Optional<CabBooking> cabBooking = cabBookings.stream()
                .filter(cab -> cab.getId().equals(cabId))
                .findFirst();

        if (cabBooking.isPresent() && "BOOKED".equalsIgnoreCase(cabBooking.get().getStatus())) {
            cabBooking.get().setStatus("CANCELLED");
            cabBooking.get().setCustomerName(null);
            cabBooking.get().setBookingTime(null);
            return cabBooking;
        }
        return Optional.empty();
    }

    public List<CabBooking> getCabsByCustomer(String customerName) {
        List<CabBooking> customerCabs = new ArrayList<>();
        for (CabBooking cabBooking : cabBookings) {
            if (customerName.equalsIgnoreCase(cabBooking.getCustomerName())) {
                customerCabs.add(cabBooking);
            }
        }
        return customerCabs;
    }

    public boolean isCabBooked(Long cabId) {
        return cabBookings.stream().anyMatch(cab -> cab.getId().equals(cabId) && "BOOKED".equalsIgnoreCase(cab.getStatus()));
    }

    public CabBooking addNewCab(String cabNumber) {
        Long newId = (long) (cabBookings.size() + 1);
        CabBooking newCab = new CabBooking(newId, cabNumber, "AVAILABLE");
        cabBookings.add(newCab);
        return newCab;
    }
}