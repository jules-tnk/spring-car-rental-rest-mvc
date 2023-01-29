package io.bootify.spring_car_rental.repos;

import io.bootify.spring_car_rental.domain.user_management.Manager;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ManagerRepository extends JpaRepository<Manager, Long> {

    List<Manager> findAllById(Long id, Sort sort);

}
