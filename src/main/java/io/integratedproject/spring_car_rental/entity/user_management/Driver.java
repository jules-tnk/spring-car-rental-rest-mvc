package io.integratedproject.spring_car_rental.entity.user_management;

import io.integratedproject.spring_car_rental.entity.CarRental;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Driver extends AppUser {

    @OneToMany(mappedBy = "driver")
    private Set<CarRental> missions;

    @Column(name = "is_available")
    private Boolean isAvailable;


    @Override
    public String getRole() {
        return UserRole.DRIVER;
    }
}
