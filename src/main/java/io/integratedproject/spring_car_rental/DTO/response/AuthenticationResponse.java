package io.integratedproject.spring_car_rental.DTO.response;

import io.integratedproject.spring_car_rental.DTO.user_management.AppUserDTO;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class AuthenticationResponse {
    private AppUserDTO user;

    private String accessToken;
}
