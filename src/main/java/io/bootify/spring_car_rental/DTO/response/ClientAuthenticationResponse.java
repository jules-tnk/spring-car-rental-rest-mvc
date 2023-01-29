package io.bootify.spring_car_rental.DTO.response;

import io.bootify.spring_car_rental.DTO.user_management.ClientDTO;
import io.bootify.spring_car_rental.domain.user_management.Client;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
public class ClientAuthenticationResponse {
    private ClientDTO user;

    private String accessToken;
}
