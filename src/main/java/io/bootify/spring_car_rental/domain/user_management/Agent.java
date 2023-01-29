package io.bootify.spring_car_rental.domain.user_management;

import io.bootify.spring_car_rental.domain.Agency;
import io.bootify.spring_car_rental.domain.CarRental;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class Agent extends AppUser {

    @ManyToOne(fetch = FetchType.LAZY)
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
