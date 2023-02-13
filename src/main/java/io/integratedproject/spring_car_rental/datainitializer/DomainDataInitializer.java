package io.integratedproject.spring_car_rental.datainitializer;

import io.integratedproject.spring_car_rental.entity.Agency;
import io.integratedproject.spring_car_rental.entity.Car;
import io.integratedproject.spring_car_rental.entity.CarDescription;
import io.integratedproject.spring_car_rental.entity.user_management.Agent;
import io.integratedproject.spring_car_rental.entity.user_management.Client;
import io.integratedproject.spring_car_rental.entity.user_management.Driver;
import io.integratedproject.spring_car_rental.entity.user_management.Manager;
import io.integratedproject.spring_car_rental.repository.*;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


@Component
public class DomainDataInitializer implements ApplicationListener<ContextRefreshedEvent> {
    boolean alreadySetup = false;

    private final CarDescriptionRepository carDescriptionRepository;
    private final CarRepository carRepository;
    private final AgencyRepository agencyRepository;
    private final AgentRepository agentRepository;
    private final ManagerRepository managerRepository;
    private final PasswordEncoder passwordEncoder;
    private final DriverRepository driverRepository;
    private final ClientRepository clientRepository;

    public DomainDataInitializer(CarDescriptionRepository carDescriptionRepository,
                                 CarRepository carRepository,
                                 AgencyRepository agencyRepository,
                                 AgentRepository agentRepository,
                                 ManagerRepository managerRepository, PasswordEncoder passwordEncoder,
                                 DriverRepository driverRepository,
                                 ClientRepository clientRepository) {
        this.carDescriptionRepository = carDescriptionRepository;
        this.carRepository = carRepository;
        this.agencyRepository = agencyRepository;
        this.agentRepository = agentRepository;
        this.managerRepository = managerRepository;
        this.passwordEncoder = passwordEncoder;
        this.driverRepository = driverRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {return;}
        initializeCars();
        initializeAgency();
        initializeUsers();

        alreadySetup = true;
    }

    private Integer generateRandomNumber(int min, int max){
        return (int) ((Math.random() * (max - min)) + min);
    }

    private void initializeCars(){
        initializeBMW();
        initializeMercedes();
    }

    private void initializeAgency(){
        agencyRepository.save(new Agency("Central agency",
                "Av. Ibn Sina, Agdal-Rabat",
                "Rabat"));
    }

    private void initializeBMW(){
        CarDescription carDescription = carDescriptionRepository.save(
                new CarDescription("BMW",
                        "BMW",
                        "Grey",
                        "https://media.ed.edmunds-media.com/mercedes-benz/amg-gt/2020/oem/2020_mercedes-benz_amg-gt_sedan_53_fq_oem_1_1600.jpg",
                        10.0*generateRandomNumber(2, 15),
                        100.0*generateRandomNumber(2, 15),
                        240,
                        350,
                        4)
        );

        for (int j = 0; j < 7; j++) {
            carRepository.save(
                    new Car(
                            carDescription,
                            "AB00"+j,
                            false
                    )
            );
        }

        for (int k = 8; k < 15; k++) {
            carRepository.save(
                    new Car(
                            carDescription,
                            "BA00"+k,
                            true
                    )
            );
        }
    }

    private void initializeMercedes() {
        CarDescription carDescription = carDescriptionRepository.save(
                new CarDescription("Mercedes",
                        "Mercedes",
                        "Red",
                        "https://hips.hearstapps.com/hmg-prod/images/2019-mercedes-benz-a220-4matic-mmp-1-1638557009.jpg?crop=0.998xw:0.750xh;0,0.130xh&resize=1200:*",
                        10.0*generateRandomNumber(3, 55),
                        100.0*generateRandomNumber(3, 55),
                        240,
                        350,
                        4)
        );

        for (int j = 0; j < 7; j++) {
            carRepository.save(
                    new Car(
                            carDescription,
                            "HJ00" + j,
                            false
                    )
            );
        }

        for (int k = 8; k < 15; k++) {
            carRepository.save(
                    new Car(
                            carDescription,
                            "RY00" + k,
                            true
                    )
            );
        }
    }

    private void initializeUsers(){
        //create client
        Client client = new Client();
        client.setFirstName("Erl");
        client.setLastName("Rab");
        client.setEmail("erl@gmail.com");
        client.setPhoneNumber(602698541);
        client.setPassword(passwordEncoder.encode("sacarina"));
        clientRepository.save(client);

        //retrieve main agency
        Agency centralAgency = agencyRepository.findFirstByNameLike("Central agency");

        //create agent
        Agent agentOne = new Agent();
        agentOne.setFirstName("Bob");
        agentOne.setLastName("Red");
        agentOne.setEmail("bob@gmail.com");
        agentOne.setPhoneNumber(702698541);
        agentOne.setPassword(passwordEncoder.encode("sacarina"));
        agentOne.setAgency(centralAgency);

        Agent agentTwo = new Agent();
        agentTwo.setFirstName("Noa");
        agentTwo.setLastName("Kol");
        agentTwo.setEmail("noa@gmail.com");
        agentTwo.setPhoneNumber(702698558);
        agentTwo.setPassword(passwordEncoder.encode("sacarina"));
        agentTwo.setAgency(centralAgency);
        agentRepository.save(agentTwo);

        //create manager
        Manager manager = new Manager();
        manager.setFirstName("Boss");
        manager.setLastName("Han");
        manager.setEmail("boss@gmail.com");
        manager.setPhoneNumber(702698558);
        manager.setAgency(centralAgency);
        manager.setPassword(passwordEncoder.encode("sacarina"));
        managerRepository.save(manager);

        //create driver
        Driver driver = new Driver();
        driver.setFirstName("Ted");
        driver.setLastName("Bil");
        driver.setEmail("ted@gmail.com");
        driver.setPhoneNumber(702674558);
        driver.setIsAvailable(true);
        driver.setPassword(passwordEncoder.encode("sacarina"));
        driverRepository.save(driver);
    }
}
