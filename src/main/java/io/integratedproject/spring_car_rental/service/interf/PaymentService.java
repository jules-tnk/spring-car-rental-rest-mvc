package io.integratedproject.spring_car_rental.service.interf;

import io.integratedproject.spring_car_rental.DTO.PaymentDTO;
import java.util.List;


public interface PaymentService {

    List<PaymentDTO> findAll();

    PaymentDTO get(final Long id);

    Long create(final PaymentDTO paymentDTO);

    void update(final Long id, final PaymentDTO paymentDTO);

    void delete(final Long id);

}
