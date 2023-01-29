package io.bootify.spring_car_rental.domain.user_management;

import io.bootify.spring_car_rental.domain.CarRental;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Driver extends AppUser {

    @OneToMany(mappedBy = "driver")
    private Set<CarRental> driverCarRentals;

    @Override
    public String getRole() {
        return UserRole.DRIVER;
    }
}
