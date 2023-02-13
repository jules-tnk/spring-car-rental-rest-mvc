package io.integratedproject.spring_car_rental.service.impl;

import io.integratedproject.spring_car_rental.entity.CarRental;
import io.integratedproject.spring_car_rental.entity.Payment;
import io.integratedproject.spring_car_rental.DTO.PaymentDTO;
import io.integratedproject.spring_car_rental.repository.CarRentalRepository;
import io.integratedproject.spring_car_rental.repository.PaymentRepository;
import io.integratedproject.spring_car_rental.service.interf.PaymentService;
import io.integratedproject.spring_car_rental.util.NotFoundException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final CarRentalRepository carRentalRepository;

    public PaymentServiceImpl(final PaymentRepository paymentRepository,
            final CarRentalRepository carRentalRepository) {
        this.paymentRepository = paymentRepository;
        this.carRentalRepository = carRentalRepository;
    }

    @Override
    public List<PaymentDTO> findAll() {
        final List<Payment> payments = paymentRepository.findAll(Sort.by("id"));
        return payments.stream()
                .map((payment) -> mapToDTO(payment, new PaymentDTO()))
                .collect(Collectors.toList());
    }

    @Override
    public PaymentDTO get(final Long id) {
        return paymentRepository.findById(id)
                .map(payment -> mapToDTO(payment, new PaymentDTO()))
                .orElseThrow(() -> new NotFoundException());
    }

    @Override
    public Long create(final PaymentDTO paymentDTO) {
        final Payment payment = new Payment();
        mapToEntity(paymentDTO, payment);
        return paymentRepository.save(payment).getId();
    }

    @Override
    public void update(final Long id, final PaymentDTO paymentDTO) {
        final Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new NotFoundException());
        mapToEntity(paymentDTO, payment);
        paymentRepository.save(payment);
    }

    @Override
    public void delete(final Long id) {
        paymentRepository.deleteById(id);
    }

    PaymentDTO mapToDTO(final Payment payment, final PaymentDTO paymentDTO) {
        paymentDTO.setId(payment.getId());
        paymentDTO.setAmount(payment.getAmount());
        paymentDTO.setDate(payment.getDate());
        paymentDTO.setMethod(payment.getMethod());
        paymentDTO.setCarRentalId(payment.getCarRental() == null ? null : payment.getCarRental().getId());
        return paymentDTO;
    }

    private Payment mapToEntity(final PaymentDTO paymentDTO, final Payment payment) {
        payment.setAmount(paymentDTO.getAmount());
        payment.setDate(paymentDTO.getDate());
        payment.setMethod(paymentDTO.getMethod());
        final CarRental carRental = paymentDTO.getCarRentalId() == null ? null : carRentalRepository.findById(paymentDTO.getCarRentalId())
                .orElseThrow(() -> new NotFoundException("payment not found"));
        payment.setCarRental(carRental);
        return payment;
    }

}
