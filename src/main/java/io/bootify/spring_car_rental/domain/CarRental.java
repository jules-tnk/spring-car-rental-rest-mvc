package io.bootify.spring_car_rental.domain;

import io.bootify.spring_car_rental.domain.user_management.Agent;
import io.bootify.spring_car_rental.domain.user_management.AppUser;
import io.bootify.spring_car_rental.domain.user_management.Driver;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import java.time.LocalDate;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
public class CarRental {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Long id;

    @Column
    private LocalDate startDate;

    @Column
    private LocalDate endDate;

    @Column
    private Boolean isPaymentCompleted;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RentalStatus status;

    @Column
    private Boolean isWithDriver;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @OneToMany(mappedBy = "carRental")
    private Set<Payment> paymentPayments;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tenant_id", nullable = false)
    private AppUser tenant;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agent_for_confirmation_id")
    private Agent agentForConfirmation;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "agent_for_return_id")
    private Agent agentForReturn;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "driver_id")
    private Driver driver;

}
