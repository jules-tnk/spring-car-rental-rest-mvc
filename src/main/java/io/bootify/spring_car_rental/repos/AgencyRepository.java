package io.bootify.spring_car_rental.repos;

import io.bootify.spring_car_rental.domain.Agency;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AgencyRepository extends JpaRepository<Agency, Long> {
}
