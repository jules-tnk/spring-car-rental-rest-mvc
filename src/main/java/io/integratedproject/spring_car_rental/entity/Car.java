package io.integratedproject.spring_car_rental.entity;

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
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
public class Car {

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

    @Column(length = 20)
    private String licensePlate;

    @Column
    private Boolean isAvailable;

    @OneToMany(mappedBy = "car")
    private Set<CarRental> carCarRentals;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_description_id", nullable = false)
    private CarDescription carDescription;

    public Car(CarDescription carDescription,
               String licensePlate,
               boolean isAvailable) {
        this.licensePlate = licensePlate;
        this.isAvailable = isAvailable;
        this.carDescription = carDescription;
    }
}
