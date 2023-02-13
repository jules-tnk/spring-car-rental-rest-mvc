package io.integratedproject.spring_car_rental.service.interf;

import io.integratedproject.spring_car_rental.DTO.user_management.DriverDTO;
import java.util.List;


public interface DriverService {

    List<DriverDTO> findAll(final String filter);

    DriverDTO get(final Long id);

    Long create(final DriverDTO driverDTO);

    void update(final Long id, final DriverDTO driverDTO);

    void delete(final Long id);

    String getReferencedWarning(final Long id);

}
