package io.integratedproject.spring_car_rental.service.interf;

import io.integratedproject.spring_car_rental.DTO.user_management.ManagerDTO;
import java.util.List;


public interface ManagerService {

    List<ManagerDTO> findAll(final String filter);

    ManagerDTO get(final Long id);

    Long create(final ManagerDTO managerDTO);

    void update(final Long id, final ManagerDTO managerDTO);

    void delete(final Long id);

    String getReferencedWarning(final Long id);

}
