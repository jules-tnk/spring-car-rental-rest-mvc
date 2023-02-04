package io.bootify.spring_car_rental.DTO.response;

import io.bootify.spring_car_rental.domain.RentalStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;


@Getter
@Setter
public class CarRentalResponseDTO {
    private Long id;
    private String carModel;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isPaymentCompleted;
    private RentalStatus status;
    private Boolean isWithDriver;
    private String tenantEmail;
    private String driverEmail;
}
