package io.bootify.spring_car_rental.repos;

import io.bootify.spring_car_rental.domain.user_management.AppUser;
import io.bootify.spring_car_rental.domain.user_management.Client;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ClientRepository extends JpaRepository<Client, Long> {

    List<Client> findAllById(Long id, Sort sort);
    boolean existsByEmailIgnoreCase(String email);

    AppUser findByEmail(String email);

    Client findByEmailIgnoreCase(String email);
}
