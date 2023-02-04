package io.bootify.spring_car_rental.repos;

import io.bootify.spring_car_rental.domain.Agency;
import io.bootify.spring_car_rental.domain.user_management.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface AgencyRepository extends JpaRepository<Agency, Long> {
    Agency findFirstByNameLike(String name);
}
