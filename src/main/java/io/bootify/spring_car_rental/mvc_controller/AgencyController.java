package io.bootify.spring_car_rental.mvc_controller;

import io.bootify.spring_car_rental.domain.user_management.Manager;
import io.bootify.spring_car_rental.DTO.AgencyDTO;
import io.bootify.spring_car_rental.repos.ManagerRepository;
import io.bootify.spring_car_rental.service.interf.AgencyService;
import io.bootify.spring_car_rental.util.WebUtils;
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
@RequestMapping("/agencys")
public class AgencyController {

    private final AgencyService agencyService;
    private final ManagerRepository managerRepository;

    public AgencyController(final AgencyService agencyService,
            final ManagerRepository managerRepository) {
        this.agencyService = agencyService;
        this.managerRepository = managerRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("managerValues", managerRepository.findAll(Sort.by("id"))
                .stream()
                .collect(Collectors.toMap(Manager::getId, Manager::getFirstName)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("agencys", agencyService.findAll());
        return "agency/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("agency") final AgencyDTO agencyDTO) {
        return "agency/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("agency") @Valid final AgencyDTO agencyDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "agency/add";
        }
        agencyService.create(agencyDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("agency.create.success"));
        return "redirect:/agencys";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("agency", agencyService.get(id));
        return "agency/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("agency") @Valid final AgencyDTO agencyDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "agency/edit";
        }
        agencyService.update(id, agencyDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("agency.update.success"));
        return "redirect:/agencys";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = agencyService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            agencyService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("agency.delete.success"));
        }
        return "redirect:/agencys";
    }

}
