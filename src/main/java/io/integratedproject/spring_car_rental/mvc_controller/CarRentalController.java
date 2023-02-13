package io.integratedproject.spring_car_rental.mvc_controller;

import io.integratedproject.spring_car_rental.entity.RentalStatus;
import io.integratedproject.spring_car_rental.entity.user_management.Agent;
import io.integratedproject.spring_car_rental.entity.user_management.AppUser;
import io.integratedproject.spring_car_rental.entity.Car;
import io.integratedproject.spring_car_rental.entity.user_management.Driver;
import io.integratedproject.spring_car_rental.DTO.CarRentalDTO;
import io.integratedproject.spring_car_rental.repository.AgentRepository;
import io.integratedproject.spring_car_rental.repository.AppUserRepository;
import io.integratedproject.spring_car_rental.repository.CarRepository;
import io.integratedproject.spring_car_rental.repository.DriverRepository;
import io.integratedproject.spring_car_rental.service.interf.CarRentalService;
import io.integratedproject.spring_car_rental.util.WebUtils;
import jakarta.validation.Valid;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/carRentals")
public class CarRentalController {

    private final CarRentalService carRentalService;
    private final CarRepository carRepository;
    private final AppUserRepository appUserRepository;
    private final AgentRepository agentRepository;
    private final DriverRepository driverRepository;

    public CarRentalController(final CarRentalService carRentalService,
            final CarRepository carRepository, final AppUserRepository appUserRepository,
            final AgentRepository agentRepository, final DriverRepository driverRepository) {
        this.carRentalService = carRentalService;
        this.carRepository = carRepository;
        this.appUserRepository = appUserRepository;
        this.agentRepository = agentRepository;
        this.driverRepository = driverRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("statusValues", RentalStatus.values());
        model.addAttribute("carValues", carRepository.findAll(Sort.by("id"))
                .stream()
                .collect(Collectors.toMap(Car::getId, Car::getLicensePlate)));
        model.addAttribute("tenantValues", appUserRepository.findAll(Sort.by("id"))
                .stream()
                .collect(Collectors.toMap(AppUser::getId, AppUser::getFirstName)));
        model.addAttribute("agentForConfirmationValues", agentRepository.findAll(Sort.by("id"))
                .stream()
                .collect(Collectors.toMap(Agent::getId, Agent::getFirstName)));
        model.addAttribute("agentForReturnValues", agentRepository.findAll(Sort.by("id"))
                .stream()
                .collect(Collectors.toMap(Agent::getId, Agent::getFirstName)));
        model.addAttribute("driverValues", driverRepository.findAll(Sort.by("id"))
                .stream()
                .collect(Collectors.toMap(Driver::getId, Driver::getFirstName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("carRentals", carRentalService.findAll());
        return "carRental/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("carRental") final CarRentalDTO carRentalDTO) {
        return "carRental/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("carRental") @Valid final CarRentalDTO carRentalDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "carRental/add";
        }
        carRentalService.create(carRentalDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("carRental.create.success"));
        return "redirect:/carRentals";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("carRental", carRentalService.get(id));
        return "carRental/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("carRental") @Valid final CarRentalDTO carRentalDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "carRental/edit";
        }
        carRentalService.update(id, carRentalDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("carRental.update.success"));
        return "redirect:/carRentals";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = carRentalService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            carRentalService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("carRental.delete.success"));
        }
        return "redirect:/carRentals";
    }

}
