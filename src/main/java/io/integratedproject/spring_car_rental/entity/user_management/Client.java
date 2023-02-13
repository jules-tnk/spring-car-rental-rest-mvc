package io.integratedproject.spring_car_rental.entity.user_management;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Client extends AppUser{
    private boolean r;
    @Override
    public String getRole() {
        return UserRole.CLIENT;
    }
}
