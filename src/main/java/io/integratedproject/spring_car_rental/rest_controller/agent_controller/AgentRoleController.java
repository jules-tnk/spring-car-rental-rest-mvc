package io.integratedproject.spring_car_rental.rest_controller.agent_controller;

import io.integratedproject.spring_car_rental.DTO.PaymentDTO;
import io.integratedproject.spring_car_rental.DTO.response.CarRentalResponseDTO;
import io.integratedproject.spring_car_rental.entity.CarRental;
import io.integratedproject.spring_car_rental.entity.RentalStatus;
import io.integratedproject.spring_car_rental.repository.CarRentalRepository;
import io.integratedproject.spring_car_rental.repository.CarRepository;
import io.integratedproject.spring_car_rental.service.impl.CarRentalServiceImpl;
import io.integratedproject.spring_car_rental.service.impl.PaymentServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api-agent")
public class AgentRoleController {
    private CarRentalServiceImpl carRentalService;
    private PaymentServiceImpl paymentService;
    private final CarRentalRepository carRentalRepository;

    public AgentRoleController(CarRentalServiceImpl carRentalService, PaymentServiceImpl paymentService,
                               CarRentalRepository carRentalRepository) {
        this.carRentalService = carRentalService;
        this.paymentService = paymentService;
        this.carRentalRepository = carRentalRepository;
    }

    @GetMapping("/rental/history")
    public ResponseEntity<List<CarRentalResponseDTO>> getAllRentals(){
        List<CarRentalResponseDTO> carRentalHistoryResponse = carRentalService.findAllForRestResponse();

        return ResponseEntity.ok(carRentalHistoryResponse);
    }

    @GetMapping("/rental/history/{id}")
    public ResponseEntity<CarRentalResponseDTO> getCarRental(@PathVariable final Long id) {
        return ResponseEntity.ok(carRentalService.findByIdForRestResponse(id));
    }

    @PostMapping("/add/payment")
    public ResponseEntity<Void> addPayment(@RequestBody final PaymentDTO paymentDTO) {
        paymentService.create(paymentDTO);
        CarRental carRental = carRentalRepository.findById(paymentDTO.getCarRentalId()).get();
        carRental.setAlreadyPaid(carRental.getAlreadyPaid()+paymentDTO.getAmount());
        if (carRental.getAlreadyPaid() >= carRental.getTotalPrice()){
            carRental.setIsPaymentCompleted(true);
        }
        carRentalRepository.save(carRental);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/rental/cancel/{id}")
    public ResponseEntity<Void> cancelCarRental(@PathVariable("id") final Long id) {
        carRentalService.cancelCarRental(id);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @GetMapping("/rental/update-status/{id}/{status}")
    public ResponseEntity<Void> setCarRentalStatusToReserved(@PathVariable("id") final Long id,
                                                             @PathVariable("status") final String status) {
        CarRental carRental = carRentalRepository.findById(id).get();
        if (status.equalsIgnoreCase("RESERVED")) {
            carRental.setStatus(RentalStatus.RESERVED);
        } else if (status.equalsIgnoreCase("CANCELLED")) {
            carRental.setStatus(RentalStatus.CANCELED);
        } else if (status.equalsIgnoreCase("TAKEN")) {
            carRental.setStatus(RentalStatus.TAKEN);
        } else if (status.equalsIgnoreCase("RETURNED")) {
            carRental.setStatus(RentalStatus.RETURNED);
        }
        carRentalRepository.save(carRental);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

}
