package io.integratedproject.spring_car_rental.DTO.incoming_request;

import lombok.Data;

@Data
public class CarRentalRequest {
    private Long carDescriptionId;
    private String startDateString;
    private String endDateString;
    private Boolean isWithDriver;
}
