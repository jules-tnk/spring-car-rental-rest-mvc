package io.bootify.spring_car_rental.domain.user_management;

import io.bootify.spring_car_rental.domain.CarRental;
import jakarta.persistence.*;

import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class AppUser {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String email;

    @Column
    private Integer phoneNumber;

    @Column
    private String password;

    @OneToMany(mappedBy = "tenant")
    private Set<CarRental> carRentals;

    public abstract String getRole();

}
