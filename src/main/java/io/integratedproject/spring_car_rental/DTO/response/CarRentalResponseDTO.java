package io.integratedproject.spring_car_rental.DTO.response;

import io.integratedproject.spring_car_rental.DTO.PaymentDTO;
import io.integratedproject.spring_car_rental.entity.RentalStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
public class CarRentalResponseDTO {
    private Long id;
    private String carModel;
    private LocalDate startDate;
    private LocalDate endDate;
    private Double totalPrice;
    private Double alreadyPaid;
    private Boolean isPaymentCompleted;
    private RentalStatus status;
    private Boolean isWithDriver;
    private String tenantEmail;
    private Integer tenantPhoneNumber;
    private String driverEmail;
    private Integer driverPhoneNumber;

    private List<PaymentDTO> payments;
}
