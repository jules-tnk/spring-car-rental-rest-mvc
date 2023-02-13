package io.integratedproject.spring_car_rental.repository;

import io.integratedproject.spring_car_rental.entity.Agency;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgencyRepository extends JpaRepository<Agency, Long> {
    Agency findFirstByNameLike(String name);
}
