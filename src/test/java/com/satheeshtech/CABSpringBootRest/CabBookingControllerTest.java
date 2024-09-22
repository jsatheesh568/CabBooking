package com.satheeshtech.CABSpringBootRest;

import com.satheeshtech.CABSpringBootRest.controller.CabBookingController;
import com.satheeshtech.CABSpringBootRest.model.CabBooking;
import com.satheeshtech.CABSpringBootRest.service.CabBookingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CabBookingControllerTest {

    @Mock
    private CabBookingService cabBookingService;

    @InjectMocks
    private CabBookingController cabBookingController;

    private List<CabBooking> cabList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        cabList = new ArrayList<>();
        cabList.add(new CabBooking(1L, "CAB-123", "AVAILABLE"));
        cabList.add(new CabBooking(2L, "CAB-456", "BOOKED"));
    }

    @Test
    void testGetAllCabs() {
        // Arrange
        when(cabBookingService.getAllCabs()).thenReturn(cabList);

        // Act
        ResponseEntity<List<CabBooking>> response = cabBookingController.getAllCabs();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("CAB-123", response.getBody().get(0).getCabNumber());
        verify(cabBookingService, times(1)).getAllCabs();
    }


    @Test
    void testBookCabSuccess() {
        // Arrange
        CabBooking bookedCab = new CabBooking(1L, "CAB-123", "BOOKED");
        when(cabBookingService.bookCab(1L, "John")).thenReturn(Optional.of(bookedCab));

        // Act
        ResponseEntity<String> response = cabBookingController.bookCab(1L, "John");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cab booked successfully for John", response.getBody());
        verify(cabBookingService, times(1)).bookCab(1L, "John");
    }

    @Test
    void testBookCabNotFound() {
        // Arrange
        when(cabBookingService.bookCab(1L, "John")).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = cabBookingController.bookCab(1L, "John");

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Cab not available for booking", response.getBody()); // Assert the actual error message
        verify(cabBookingService, times(1)).bookCab(1L, "John");
    }


    @Test
    void testCancelBookingSuccess() {
        // Arrange
        CabBooking cancelledCab = new CabBooking(1L, "CAB-123", "CANCELLED");
        when(cabBookingService.cancelBooking(1L)).thenReturn(Optional.of(cancelledCab));

        // Act
        ResponseEntity<String> response = cabBookingController.cancelBooking(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("CANCELLED", response.getBody());
        verify(cabBookingService, times(1)).cancelBooking(1L);
    }

    @Test
    void testCancelBookingNotFound() {
        // Arrange
        when(cabBookingService.cancelBooking(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<String> response = cabBookingController.cancelBooking(1L);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Cab booking could not be cancelled", response.getBody()); // Check for the actual error message
        verify(cabBookingService, times(1)).cancelBooking(1L);
    }


    @Test
    void testGetCabsByCustomerSuccess() {
        // Arrange
        List<CabBooking> customerCabs = new ArrayList<>();
        customerCabs.add(new CabBooking(1L, "CAB-123", "BOOKED"));
        when(cabBookingService.getCabsByCustomer("John")).thenReturn(customerCabs);

        // Act
        ResponseEntity<List<CabBooking>> response = cabBookingController.getCabsByCustomer("John");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("BOOKED", response.getBody().get(0).getStatus());
        verify(cabBookingService, times(1)).getCabsByCustomer("John");
    }


    @Test
    void testGetCabsByCustomerEmpty() {
        // Arrange
        when(cabBookingService.getCabsByCustomer("John")).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<CabBooking>> response = cabBookingController.getCabsByCustomer("John");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(cabBookingService, times(1)).getCabsByCustomer("John");
    }



    @Test
    void testIsCabBookedTrue() {
        // Arrange
        when(cabBookingService.isCabBooked(1L)).thenReturn(true);

        // Act
        ResponseEntity<String> response = cabBookingController.isCabBooked(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cab is already booked", response.getBody());
        verify(cabBookingService, times(1)).isCabBooked(1L);
    }

    @Test
    void testIsCabBookedFalse() {
        // Arrange
        when(cabBookingService.isCabBooked(1L)).thenReturn(false);

        // Act
        ResponseEntity<String> response = cabBookingController.isCabBooked(1L);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Cab is available", response.getBody()); // Adjust to match the actual response from the controller
        verify(cabBookingService, times(1)).isCabBooked(1L);
    }


    @Test
    void testAddNewCabSuccess() {
        // Arrange
        CabBooking newCab = new CabBooking(3L, "CAB-789", "AVAILABLE");
        when(cabBookingService.addNewCab("CAB-789")).thenReturn(newCab);

        // Act
        ResponseEntity<String> response = cabBookingController.addNewCab("CAB-789");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("AVAILABLE", response.getBody());
        verify(cabBookingService, times(1)).addNewCab("CAB-789");
    }

    @Test
    void testAddNewCabFail() {
        // Arrange
        when(cabBookingService.addNewCab("CAB-000")).thenReturn(null);

        // Act
        ResponseEntity<String> response = cabBookingController.addNewCab("CAB-000");

        // Assert
        assertEquals("Failed to add new cab", response.getBody()); // Check the error message
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(cabBookingService, times(1)).addNewCab("CAB-000");
    }

}
