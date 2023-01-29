package io.bootify.spring_car_rental.service.interf;

import io.bootify.spring_car_rental.DTO.CarRentalDTO;
import java.util.List;


public interface CarRentalService {

    List<CarRentalDTO> findAll();

    CarRentalDTO get(final Long id);

    Long create(final CarRentalDTO carRentalDTO);

    void update(final Long id, final CarRentalDTO carRentalDTO);

    void delete(final Long id);

    String getReferencedWarning(final Long id);

}
