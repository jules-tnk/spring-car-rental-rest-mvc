package io.integratedproject.spring_car_rental.entity.user_management;

import io.integratedproject.spring_car_rental.entity.Agency;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Manager extends AppUser {

    @OneToOne(mappedBy = "manager", fetch = FetchType.EAGER)
    private Agency agency;

    @Override
    public String getRole() {
        return UserRole.MANAGER;
    }
}
