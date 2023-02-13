package io.integratedproject.spring_car_rental.DTO.response;

import java.util.List;

import io.integratedproject.spring_car_rental.DTO.FieldError;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ErrorResponse {

    private Integer httpStatus;
    private String exception;
    private String message;
    private List<FieldError> fieldErrors;

}
