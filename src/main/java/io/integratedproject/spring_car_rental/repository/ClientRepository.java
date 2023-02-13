package io.integratedproject.spring_car_rental.repository;

import io.integratedproject.spring_car_rental.entity.user_management.AppUser;
import io.integratedproject.spring_car_rental.entity.user_management.Client;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findAllById(Long id, Sort sort);
    boolean existsByEmailIgnoreCase(String email);

    AppUser findByEmail(String email);

    Client findByEmailIgnoreCase(String email);
}
