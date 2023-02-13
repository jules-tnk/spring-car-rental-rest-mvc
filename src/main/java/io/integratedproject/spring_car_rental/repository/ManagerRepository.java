package io.integratedproject.spring_car_rental.repository;

import io.integratedproject.spring_car_rental.entity.user_management.Manager;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    List<Manager> findAllById(Long id, Sort sort);

}
