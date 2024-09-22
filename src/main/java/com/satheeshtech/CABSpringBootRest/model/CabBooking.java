package com.satheeshtech.CABSpringBootRest.model;

public class CabBooking {

    private Long id;
        private String cabNumber;
        private String customerName;
        private String status; // AVAILABLE, BOOKED, CANCELLED
        private String bookingTime;

        // Constructors
        public CabBooking() {
        }

    public CabBooking(Long id, String cabNumber, String status) {
        this.id = id;
        this.cabNumber = cabNumber;
        this.status = status;
        this.customerName = null;
        this.bookingTime = null;
    }

    // Getters and Setters
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getCabNumber() {
            return cabNumber;
        }

        public void setCabNumber(String cabNumber) {
            this.cabNumber = cabNumber;
        }

        public String getCustomerName() {
            return customerName;
        }

        public void setCustomerName(String customerName) {
            this.customerName = customerName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getBookingTime() {
            return bookingTime;
        }

        public void setBookingTime(String bookingTime) {
            this.bookingTime = bookingTime;
        }
    }

