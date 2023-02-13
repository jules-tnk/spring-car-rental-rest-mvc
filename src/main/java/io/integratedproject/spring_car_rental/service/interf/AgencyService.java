package io.integratedproject.spring_car_rental.service.interf;

import io.integratedproject.spring_car_rental.DTO.AgencyDTO;
import java.util.List;


public interface AgencyService {

    List<AgencyDTO> findAll();

    AgencyDTO get(final Long id);

    Long create(final AgencyDTO agencyDTO);

    void update(final Long id, final AgencyDTO agencyDTO);

    void delete(final Long id);

    String getReferencedWarning(final Long id);

}
