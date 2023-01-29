package io.bootify.spring_car_rental.service.interf;

import io.bootify.spring_car_rental.DTO.CarDescriptionDTO;
import java.util.List;


public interface CarDescriptionService {

    List<CarDescriptionDTO> findAll(final String filter);

    CarDescriptionDTO get(final Long id);

    Long create(final CarDescriptionDTO carDescriptionDTO);

    void update(final Long id, final CarDescriptionDTO carDescriptionDTO);

    void delete(final Long id);

    String getReferencedWarning(final Long id);

}
