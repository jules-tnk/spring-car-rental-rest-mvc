package io.bootify.spring_car_rental.repos;

import io.bootify.spring_car_rental.domain.user_management.Driver;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;


public interface DriverRepository extends JpaRepository<Driver, Long> {

    List<Driver> findAllById(Long id, Sort sort);

}
