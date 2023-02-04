package io.bootify.spring_car_rental.DTO.user_management;

import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class AppUserDTO {

    private Long id;

    @Size(max = 255)
    private String firstName;

    @Size(max = 255)
    private String lastName;

    @Size(max = 255)
    private String email;

    private Integer phoneNumber;

    @Size(max = 255)
    private String password;

    private String role;

}
