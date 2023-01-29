package io.bootify.spring_car_rental.repos;

import io.bootify.spring_car_rental.domain.user_management.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

}
