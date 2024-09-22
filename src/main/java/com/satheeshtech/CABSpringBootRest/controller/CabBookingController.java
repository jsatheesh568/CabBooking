package com.satheeshtech.CABSpringBootRest.controller;

import com.satheeshtech.CABSpringBootRest.model.CabBooking;
import com.satheeshtech.CABSpringBootRest.service.CabBookingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/cabs")
public class CabBookingController {

    @Autowired
    private CabBookingService cabBookingService;

    // Get all cabs
    @Operation(summary = "Get all cabs", description = "Returns a list of all cabs with their current status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "204", description = "No cabs available")
    })
    @GetMapping("/all-cab-details")
    public ResponseEntity<List<CabBooking>> getAllCabs() {
        List<CabBooking> allCabs = cabBookingService.getAllCabs();
        if (allCabs.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(allCabs);
    }

    // Book a cab by id
    @Operation(summary = "Book a cab", description = "Books a cab with the given ID for a specific customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cab booked successfully"),
            @ApiResponse(responseCode = "400", description = "Cab not available for booking")
    })
    @PostMapping("/book/{id}")
    public ResponseEntity<String> bookCab(@PathVariable Long id, @RequestParam String customerName) {
        Optional<CabBooking> booking = cabBookingService.bookCab(id, customerName);
        if (booking.isPresent()) {
            return ResponseEntity.ok("Cab booked successfully for " + customerName);
        }
        return ResponseEntity.status(400).body("Cab not available for booking");
    }

    // Cancel a cab booking by id
    @Operation(summary = "Cancel cab booking", description = "Cancels the booking of the cab with the given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Booking cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Cab booking could not be cancelled")
    })
    @PostMapping("/cancel/{id}")
    public ResponseEntity<String> cancelBooking(@PathVariable Long bookingId) {
        Optional<CabBooking> cancelledCab = cabBookingService.cancelBooking(bookingId);
        if (cancelledCab.isPresent()) {
            return ResponseEntity.ok("CANCELLED");
        } else {
            return ResponseEntity.badRequest().body("Cab booking could not be cancelled");
        }
    }

    // Get cabs by customer name
    @Operation(summary = "Get cabs by customer", description = "Returns a list of cabs booked by a specific customer")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved cabs"),
            @ApiResponse(responseCode = "204", description = "No cabs found for this customer")
    })
    @GetMapping("/customer/{customerName}")
    public ResponseEntity<List<CabBooking>> getCabsByCustomer(@PathVariable String customerName) {
        List<CabBooking> cabs = cabBookingService.getCabsByCustomer(customerName);
        return ResponseEntity.ok(cabs); // This will return 200 OK even if the list is empty
    }

    // Check if a cab is booked
    @Operation(summary = "Check if a cab is booked", description = "Checks whether the cab with the given ID is booked or available")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns cab status")
    })
    @GetMapping("/is-booked/{id}")
    public ResponseEntity<String> isCabBooked(@PathVariable Long id) {
        boolean isBooked = cabBookingService.isCabBooked(id);
        if (isBooked) {
            return ResponseEntity.ok("Cab is already booked");
        } else {
            return ResponseEntity.ok("Cab is available");
        }
    }

    // Admin endpoint to add a new cab
    @Operation(summary = "Add a new cab", description = "Admin endpoint to add a new cab to the system")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New cab added successfully")
    })
    @PostMapping("/admin/add-cab")
    public ResponseEntity<String> addNewCab(String cabNumber) {
        CabBooking newCab = cabBookingService.addNewCab(cabNumber);
        if (newCab == null) {
            return new ResponseEntity<>("Failed to add new cab", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(newCab.getStatus(), HttpStatus.OK);
    }

}