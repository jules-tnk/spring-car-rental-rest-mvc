package io.integratedproject.spring_car_rental.repository;

import io.integratedproject.spring_car_rental.entity.user_management.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    AppUser findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

}
