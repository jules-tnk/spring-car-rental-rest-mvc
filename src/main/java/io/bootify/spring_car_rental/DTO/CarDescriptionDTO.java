package io.bootify.spring_car_rental.DTO;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CarDescriptionDTO {

    private Long id;

    @Size(max = 255)
    private String model;

    @Size(max = 255)
    private String brand;

    @Size(max = 255)
    private String color;

    @Size(max = 255)
    private String imgUrl;

    private Double pricePerHour;

    private Integer maxSpeed;

    private Integer maxPower;

    private Integer seatNumber;

}
