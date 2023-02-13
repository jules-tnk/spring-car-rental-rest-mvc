package io.integratedproject.spring_car_rental.mvc_controller;

import io.integratedproject.spring_car_rental.DTO.user_management.DriverDTO;
import io.integratedproject.spring_car_rental.service.interf.DriverService;
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
@RequestMapping("/drivers")
public class DriverController {

    private final DriverService driverService;

    public DriverController(final DriverService driverService) {
        this.driverService = driverService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter, final Model model) {
        model.addAttribute("drivers", driverService.findAll(filter));
        model.addAttribute("filter", filter);
        return "driver/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("driver") final DriverDTO driverDTO) {
        return "driver/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("driver") @Valid final DriverDTO driverDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "driver/add";
        }
        driverService.create(driverDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("driver.create.success"));
        return "redirect:/drivers";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("driver", driverService.get(id));
        return "driver/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("driver") @Valid final DriverDTO driverDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "driver/edit";
        }
        driverService.update(id, driverDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("driver.update.success"));
        return "redirect:/drivers";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = driverService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            driverService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("driver.delete.success"));
        }
        return "redirect:/drivers";
    }

}
