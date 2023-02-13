package io.integratedproject.spring_car_rental.service.interf;

import io.integratedproject.spring_car_rental.DTO.user_management.ClientDTO;
import java.util.List;


public interface ClientService {

    List<ClientDTO> findAll(final String filter);

    ClientDTO get(final Long id);

    ClientDTO findByEmail(String email);

    Long create(final ClientDTO clientDTO);

    void update(final Long id, final ClientDTO clientDTO);

    void delete(final Long id);

}
