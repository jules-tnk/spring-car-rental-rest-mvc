package io.integratedproject.spring_car_rental.entity.user_management;

import io.integratedproject.spring_car_rental.entity.Agency;
import io.integratedproject.spring_car_rental.entity.CarRental;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Agent extends AppUser {

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agency_id", nullable = false)
    private Agency agency;

    @OneToMany(mappedBy = "agentForConfirmation")
    private Set<CarRental> agentForConfirmationCarRentals;

    @OneToMany(mappedBy = "agentForReturn")
    private Set<CarRental> agentForReturnCarRentals;

    @Override
    public String getRole() {
        return UserRole.AGENT;
    }


}
