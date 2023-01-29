package io.bootify.spring_car_rental.repos;

import io.bootify.spring_car_rental.domain.CarDescription;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CarDescriptionRepository extends JpaRepository<CarDescription, Long> {

    List<CarDescription> findAllById(Long id, Sort sort);

}
