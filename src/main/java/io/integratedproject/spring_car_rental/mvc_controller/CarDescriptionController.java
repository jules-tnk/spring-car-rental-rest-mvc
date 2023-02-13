package io.integratedproject.spring_car_rental.mvc_controller;

import io.integratedproject.spring_car_rental.DTO.CarDescriptionDTO;
import io.integratedproject.spring_car_rental.service.interf.CarDescriptionService;
import io.integratedproject.spring_car_rental.util.WebUtils;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/carDescriptions")
public class CarDescriptionController {

    private final CarDescriptionService carDescriptionService;

    public CarDescriptionController(final CarDescriptionService carDescriptionService) {
        this.carDescriptionService = carDescriptionService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter, final Model model) {
        model.addAttribute("carDescriptions", carDescriptionService.findAll(filter));
        model.addAttribute("filter", filter);
        return "carDescription/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("carDescription") final CarDescriptionDTO carDescriptionDTO) {
        return "carDescription/add";
    }

    @PostMapping("/add")
    public String add(
            @ModelAttribute("carDescription") @Valid final CarDescriptionDTO carDescriptionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "carDescription/add";
        }
        carDescriptionService.create(carDescriptionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("carDescription.create.success"));
        return "redirect:/carDescriptions";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("carDescription", carDescriptionService.get(id));
        return "carDescription/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("carDescription") @Valid final CarDescriptionDTO carDescriptionDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "carDescription/edit";
        }
        carDescriptionService.update(id, carDescriptionDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("carDescription.update.success"));
        return "redirect:/carDescriptions";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = carDescriptionService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            carDescriptionService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("carDescription.delete.success"));
        }
        return "redirect:/carDescriptions";
    }

}
