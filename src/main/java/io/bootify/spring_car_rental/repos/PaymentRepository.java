package io.bootify.spring_car_rental.repos;

import io.bootify.spring_car_rental.domain.Payment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
