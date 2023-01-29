package io.bootify.spring_car_rental.domain.user_management;

import io.bootify.spring_car_rental.domain.Agency;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.SequenceGenerator;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Manager extends AppUser {

    @OneToOne(mappedBy = "manager", fetch = FetchType.LAZY)
    private Agency manager;

    @Override
    public String getRole() {
        return UserRole.MANAGER;
    }
}
