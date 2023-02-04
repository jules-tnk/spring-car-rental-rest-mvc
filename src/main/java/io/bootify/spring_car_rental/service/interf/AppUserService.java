package io.bootify.spring_car_rental.service.interf;


import io.bootify.spring_car_rental.DTO.user_management.AppUserDTO;

import java.util.List;


public interface AppUserService {

    List<AppUserDTO> findAll();

    AppUserDTO get(final Long id);

    AppUserDTO getByEmail(final String email);

    void update(final Long id, final AppUserDTO appUserDTO);

    void delete(final Long id);

    /*String getReferencedWarning(final Long id);*/

}
