package io.integratedproject.spring_car_rental.DTO;

import io.integratedproject.spring_car_rental.entity.RentalStatus;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CarRentalDTO {

    private Long id;

    private LocalDate startDate;

    private LocalDate endDate;

    private Boolean isPaymentCompleted;

    @NotNull
    private RentalStatus status;

    private Boolean isWithDriver;

    @NotNull
    private Long car;

    @NotNull
    private Long tenant;

    private Long agentForConfirmation;

    private Long agentForReturn;

    private Long driver;

}
