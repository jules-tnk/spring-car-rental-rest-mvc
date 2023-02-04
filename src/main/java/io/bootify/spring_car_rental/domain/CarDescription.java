package io.bootify.spring_car_rental.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
public class CarDescription {

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
    private String model;

    @Column
    private String brand;

    @Column
    private String color;

    @Column
    private String imgUrl;

    @Column
    private Double pricePerHour;

    @Column
    private Integer maxSpeed;

    @Column
    private Integer maxPower;

    @Column
    private Integer seatNumber;

    @OneToMany(mappedBy = "carDescription")
    private Set<Car> cars;

    public CarDescription(String model,
                          String brand,
                          String color,
                          String imgUrl,
                          Double pricePerHour,
                          Integer maxSpeed,
                          Integer maxPower,
                          Integer seatNumber) {
        this.model = model;
        this.brand = brand;
        this.color = color;
        this.imgUrl = imgUrl;
        this.pricePerHour = pricePerHour;
        this.maxSpeed = maxSpeed;
        this.maxPower = maxPower;
        this.seatNumber = seatNumber;
    }
}
