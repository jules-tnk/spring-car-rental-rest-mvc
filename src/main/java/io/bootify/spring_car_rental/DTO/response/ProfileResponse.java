package io.bootify.spring_car_rental.DTO.response;

import io.bootify.spring_car_rental.DTO.CarRentalDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProfileResponse {
    private List<CarRentalResponseDTO> carRentalHistory;
    private List<CarRentalResponseDTO> missionHistory;
}
