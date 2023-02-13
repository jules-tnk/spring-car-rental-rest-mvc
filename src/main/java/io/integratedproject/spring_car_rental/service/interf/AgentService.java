package io.integratedproject.spring_car_rental.service.interf;

import io.integratedproject.spring_car_rental.DTO.user_management.AgentDTO;
import java.util.List;


public interface AgentService {

    List<AgentDTO> findAll(final String filter);

    AgentDTO get(final Long id);

    Long create(final AgentDTO agentDTO);

    void update(final Long id, final AgentDTO agentDTO);

    void delete(final Long id);

    String getReferencedWarning(final Long id);

}
