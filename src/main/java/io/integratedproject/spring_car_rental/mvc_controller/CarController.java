package io.integratedproject.spring_car_rental.mvc_controller;

import io.integratedproject.spring_car_rental.entity.CarDescription;
import io.integratedproject.spring_car_rental.DTO.CarDTO;
import io.integratedproject.spring_car_rental.repository.CarDescriptionRepository;
import io.integratedproject.spring_car_rental.service.interf.CarService;
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
@RequestMapping("/cars")
public class CarController {

    private final CarService carService;
    private final CarDescriptionRepository carDescriptionRepository;

    public CarController(final CarService carService,
            final CarDescriptionRepository carDescriptionRepository) {
        this.carService = carService;
        this.carDescriptionRepository = carDescriptionRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("carDescriptionValues", carDescriptionRepository.findAll(Sort.by("id"))
                .stream()
                .collect(Collectors.toMap(CarDescription::getId, CarDescription::getModel)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("cars", carService.findAll());
        return "car/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("car") final CarDTO carDTO) {
        return "car/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("car") @Valid final CarDTO carDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "car/add";
        }
        carService.create(carDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("car.create.success"));
        return "redirect:/cars";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("car", carService.get(id));
        return "car/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("car") @Valid final CarDTO carDTO, final BindingResult bindingResult,
            final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "car/edit";
        }
        carService.update(id, carDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("car.update.success"));
        return "redirect:/cars";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = carService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            carService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("car.delete.success"));
        }
        return "redirect:/cars";
    }

}
