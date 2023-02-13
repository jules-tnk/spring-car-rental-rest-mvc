package io.integratedproject.spring_car_rental.service.interf;

import io.integratedproject.spring_car_rental.DTO.CarDTO;
import java.util.List;


public interface CarService {

    List<CarDTO> findAll();

    CarDTO get(final Long id);

    Long create(final CarDTO carDTO);

    void update(final Long id, final CarDTO carDTO);

    void delete(final Long id);

    String getReferencedWarning(final Long id);

}
