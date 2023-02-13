package io.integratedproject.spring_car_rental.service.impl;

import io.integratedproject.spring_car_rental.entity.user_management.Client;
import io.integratedproject.spring_car_rental.DTO.user_management.ClientDTO;
import io.integratedproject.spring_car_rental.repository.ClientRepository;
import io.integratedproject.spring_car_rental.service.interf.ClientService;
import io.integratedproject.spring_car_rental.util.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;

    public ClientServiceImpl(final ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<ClientDTO> findAll(final String filter) {
        List<Client> clients;
        final Sort sort = Sort.by("id");
        if (filter != null) {
            Long longFilter = null;
            try {
                longFilter = Long.parseLong(filter);
            } catch (final NumberFormatException numberFormatException) {
                // keep null - no parseable input
            }
            clients = clientRepository.findAllById(longFilter, sort);
        } else {
            clients = clientRepository.findAll(sort);
        }
        return clients.stream()
                .map((client) -> mapToDTO(client, new ClientDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public ClientDTO get(final Long id) {
        return clientRepository.findById(id)
                .map(client -> mapToDTO(client, new ClientDTO()))
                .orElseThrow(() -> new NotFoundException());
    }

    @Override
    public ClientDTO findByEmail(String email) {
        return mapToDTO(
                clientRepository.findByEmailIgnoreCase(email),
                new ClientDTO()
        );
    }

    @Override
    public Long create(final ClientDTO clientDTO) {
        final Client client = new Client();
        mapToEntity(clientDTO, client);
        client.setPassword(passwordEncoder.encode(clientDTO.getPassword()));
        return clientRepository.save(client).getId();
    }

    @Override
    public void update(final Long id, final ClientDTO clientDTO) {
        final Client client = clientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        mapToEntity(clientDTO, client);
        clientRepository.save(client);
    }

    @Override
    public void delete(final Long id) {
        clientRepository.deleteById(id);
    }

    private ClientDTO mapToDTO(final Client client, final ClientDTO clientDTO) {
        clientDTO.setId(client.getId());
        clientDTO.setFirstName(client.getFirstName());
        clientDTO.setLastName(client.getLastName());
        clientDTO.setEmail(client.getEmail());
        clientDTO.setPhoneNumber(client.getPhoneNumber());
        return clientDTO;
    }

    private Client mapToEntity(final ClientDTO clientDTO, final Client client) {
        client.setFirstName(clientDTO.getFirstName());
        client.setLastName(clientDTO.getLastName());
        client.setEmail(clientDTO.getEmail());
        client.setPhoneNumber(clientDTO.getPhoneNumber());
        return client;
    }

}
