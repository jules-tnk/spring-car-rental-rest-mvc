package io.integratedproject.spring_car_rental.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CarDTO {

    private Long id;

    @Size(max = 20)
    private String licensePlate;

    @Size(max = 255)
    private Boolean isAvailable;

    @NotNull
    private Long carDescription;

}
