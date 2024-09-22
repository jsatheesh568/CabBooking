package com.satheeshtech.CABSpringBootRest.repository;

import com.satheeshtech.CABSpringBootRest.model.CabBooking;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CabBookingRepository extends CrudRepository <CabBooking, Long> {

    List < CabBooking > findByStatus ( String status);

    List < CabBooking > findByCustomerName (String customerName);
}
