package io.integratedproject.spring_car_rental.DTO;


import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class PaymentDTO {
    private Long id;
    private Double amount;
    private LocalDate date;
    @Size(max = 255)
    private String method;
    private Long carRentalId;

}
