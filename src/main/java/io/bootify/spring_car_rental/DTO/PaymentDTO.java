package io.bootify.spring_car_rental.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class PaymentDTO {

    private Long id;

    private Double amount;

    private LocalDate date;

    @Size(max = 255)
    private String method;

    @NotNull
    private Long carRentalId;

}
