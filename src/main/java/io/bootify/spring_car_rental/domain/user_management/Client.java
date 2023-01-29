package io.bootify.spring_car_rental.domain.user_management;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
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
