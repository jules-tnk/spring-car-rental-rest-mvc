package io.bootify.spring_car_rental.service.impl;

import io.bootify.spring_car_rental.DTO.user_management.AppUserDTO;
import io.bootify.spring_car_rental.domain.user_management.AppUser;
import io.bootify.spring_car_rental.repos.AppUserRepository;
import io.bootify.spring_car_rental.service.interf.AppUserService;
import io.bootify.spring_car_rental.util.NotFoundException;
import io.bootify.spring_car_rental.util.WebUtils;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserServiceImpl(final AppUserRepository appUserRepository,
                              final PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<AppUserDTO> findAll() {
        final List<AppUser> appUsers = appUserRepository.findAll(Sort.by("id"));
        return appUsers.stream()
                .map((appUser) -> mapToDTO(appUser, new AppUserDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public AppUserDTO get(final Long id) {
        return appUserRepository.findById(id)
                .map(appUser -> mapToDTO(appUser, new AppUserDTO()))
                .orElseThrow(() -> new NotFoundException());
    }

    @Override
    public AppUserDTO getByEmail(String email) {
        AppUser user = appUserRepository.findByEmailIgnoreCase(email);
        if (user == null){
            throw new NotFoundException();
        }
        return mapToDTO(user, new AppUserDTO());
    }


    @Override
    public void update(final Long id, final AppUserDTO appUserDTO) {
        final AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        mapToEntity(appUserDTO, appUser);
        appUserRepository.save(appUser);
    }

    @Override
    public void delete(final Long id) {
        appUserRepository.deleteById(id);
    }

    private AppUserDTO mapToDTO(final AppUser appUser, final AppUserDTO appUserDTO) {
        appUserDTO.setId(appUser.getId());
        appUserDTO.setFirstName(appUser.getFirstName());
        appUserDTO.setLastName(appUser.getLastName());
        appUserDTO.setEmail(appUser.getEmail());
        appUserDTO.setPhoneNumber(appUser.getPhoneNumber());
        appUserDTO.setRole(appUser.getRole());
        return appUserDTO;
    }

    private AppUser mapToEntity(final AppUserDTO appUserDTO, final AppUser appUser) {
        appUser.setFirstName(appUserDTO.getFirstName());
        appUser.setLastName(appUserDTO.getLastName());
        appUser.setEmail(appUserDTO.getEmail());
        appUser.setPhoneNumber(appUserDTO.getPhoneNumber());
        appUser.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));
        return appUser;
    }

    /*@Transactional
    @Override
    public String getReferencedWarning(final Long id) {
        final AppUser appUser = appUserRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        if (!appUser.getRoles().isEmpty()) {
            return WebUtils.getMessage("appUser.role.oneToMany.referenced", appUser.getRoles().iterator().next().getId());
        }
        return null;
    }*/

}
