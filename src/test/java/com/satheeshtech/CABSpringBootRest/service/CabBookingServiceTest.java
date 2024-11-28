package com.satheeshtech.CABSpringBootRest.service;

import com.satheeshtech.CABSpringBootRest.model.CabBooking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith ( MockitoJUnitRunner.class )
class CabBookingServiceTest {

    private CabBookingService cabBookingService;

    @BeforeEach
    void setUp() {
        cabBookingService = new CabBookingService();
    }

    @Test
    void testGetAllCabs() {
        // Act
        List<CabBooking> allCabs = cabBookingService.getAllCabs();

        // Assert
        assertNotNull(allCabs);
        assertEquals(4, allCabs.size());
    }

    @Test
    void testBookCabSuccess() {
        // Act
        Optional<CabBooking> bookedCab = cabBookingService.bookCab(1L, "John");

        // Assert
        assertTrue(bookedCab.isPresent());
        assertEquals("John", bookedCab.get().getCustomerName());
        assertEquals("BOOKED", bookedCab.get().getStatus());
    }

    @Test
    void testBookCabAlreadyBooked() {
        // Arrange
        cabBookingService.bookCab(1L, "John");

        // Act
        Optional<CabBooking> result = cabBookingService.bookCab(1L, "Jane");

        // Assert
        assertFalse(result.isPresent());
    }

    @Test
    void testCancelBookingSuccess() {
        // Arrange
        cabBookingService.bookCab(1L, "John");

        // Act
        Optional<CabBooking> cancelledCab = cabBookingService.cancelBooking(1L);

        // Assert
        assertTrue(cancelledCab.isPresent());
        assertEquals("CANCELLED", cancelledCab.get().getStatus());
        assertNull(cancelledCab.get().getCustomerName());
    }

    @Test
    void testCancelBookingNotBooked() {
        // Act
        Optional<CabBooking> cancelledCab = cabBookingService.cancelBooking(1L);

        // Assert
        assertFalse(cancelledCab.isPresent());
    }

    @Test
    void testGetCabsByCustomer() {
        // Arrange
        cabBookingService.bookCab(1L, "John");

        // Act
        List<CabBooking> customerCabs = cabBookingService.getCabsByCustomer("John");

        // Assert
        assertEquals(1, customerCabs.size());
        assertEquals("John", customerCabs.get(0).getCustomerName());
    }

    @Test
    void testGetCabsByCustomerNoMatch() {
        // Act
        List<CabBooking> customerCabs = cabBookingService.getCabsByCustomer("Jane");

        // Assert
        assertTrue(customerCabs.isEmpty());
    }

    @Test
    void testIsCabBookedTrue() {
        // Arrange
        cabBookingService.bookCab(1L, "John");

        // Act
        boolean isBooked = cabBookingService.isCabBooked(1L);

        // Assert
        assertTrue(isBooked);
    }

    @Test
    void testIsCabBookedFalse() {
        // Act
        boolean isBooked = cabBookingService.isCabBooked(1L);

        // Assert
        assertFalse(isBooked);
    }

    @Test
    void testAddNewCab() {
        // Act
        CabBooking newCab = cabBookingService.addNewCab("CAB-999");

        // Assert
        assertNotNull(newCab);
        assertEquals("CAB-999", newCab.getCabNumber());
        assertEquals("AVAILABLE", newCab.getStatus());

        // Verify the list size increased
        assertEquals(5, cabBookingService.getAllCabs().size());
    }
}
