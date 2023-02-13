package io.integratedproject.spring_car_rental.repository;

import io.integratedproject.spring_car_rental.entity.CarDescription;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarDescriptionRepository extends JpaRepository<CarDescription, Long> {

    List<CarDescription> findAllById(Long id, Sort sort);

}
