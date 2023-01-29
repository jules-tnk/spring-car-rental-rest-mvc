package io.bootify.spring_car_rental.datainitializer;

import io.bootify.spring_car_rental.domain.Car;
import io.bootify.spring_car_rental.domain.CarDescription;
import io.bootify.spring_car_rental.repos.CarDescriptionRepository;
import io.bootify.spring_car_rental.repos.CarRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class DomainDataInitializer implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;

    private final CarDescriptionRepository carDescriptionRepository;
    private final CarRepository carRepository;

    public DomainDataInitializer(CarDescriptionRepository carDescriptionRepository,
                                 CarRepository carRepository) {
        this.carDescriptionRepository = carDescriptionRepository;
        this.carRepository = carRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {return;}

        for (int i = 0; i < 15; i++) {
            carDescriptionRepository.save(
                    new CarDescription("Model "+i,
                            "Brand "+i,
                            "Color "+i,
                            "https://media.ed.edmunds-media.com/mercedes-benz/amg-gt/2020/oem/2020_mercedes-benz_amg-gt_sedan_53_fq_oem_1_1600.jpg",
                            100.0*i,
                            10*i,
                            20*i,
                            4)
            );
        }

        CarDescription carDescriptionWithCars = carDescriptionRepository.save(
                new CarDescription("Model X",
                        "BMW",
                        "Grey",
                        "https://media.ed.edmunds-media.com/mercedes-benz/amg-gt/2020/oem/2020_mercedes-benz_amg-gt_sedan_53_fq_oem_1_1600.jpg",
                        100.0,
                        240,
                        350,
                        4)
        );

        for (int i = 0; i < 7; i++) {
            carRepository.save(
                    new Car(
                            carDescriptionWithCars,
                            "AB00"+i,
                            false
                    )
            );
        }

        for (int i = 8; i < 15; i++) {
            carRepository.save(
                    new Car(
                            carDescriptionWithCars,
                            "BA00"+i,
                            true
                    )
            );
        }


        alreadySetup = false;
    }
}
