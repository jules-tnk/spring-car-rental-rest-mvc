package io.integratedproject.spring_car_rental.service.interf;

import io.integratedproject.spring_car_rental.DTO.CarRentalDTO;
import io.integratedproject.spring_car_rental.DTO.response.CarRentalResponseDTO;

import java.util.List;


public interface CarRentalService {

    List<CarRentalDTO> findAll();

    List<CarRentalResponseDTO> findByTenantEmail(String email);

    List<CarRentalResponseDTO> findByDriverEmail(String email);

    CarRentalDTO get(final Long id);

    CarRentalResponseDTO findByIdForRestResponse(Long id);

    Long create(final CarRentalDTO carRentalDTO);

    void update(final Long id, final CarRentalDTO carRentalDTO);

    void delete(final Long id);

    String getReferencedWarning(final Long id);

    void cancelCarRental(Long id);
}
