package io.integratedproject.spring_car_rental.mvc_controller;

import io.integratedproject.spring_car_rental.DTO.user_management.ManagerDTO;
import io.integratedproject.spring_car_rental.service.interf.ManagerService;
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
@RequestMapping("/managers")
public class ManagerController {

    private final ManagerService managerService;

    public ManagerController(final ManagerService managerService) {
        this.managerService = managerService;
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter, final Model model) {
        model.addAttribute("managers", managerService.findAll(filter));
        model.addAttribute("filter", filter);
        return "manager/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("manager") final ManagerDTO managerDTO) {
        return "manager/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("manager") @Valid final ManagerDTO managerDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "manager/add";
        }
        managerService.create(managerDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("manager.create.success"));
        return "redirect:/managers";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("manager", managerService.get(id));
        return "manager/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("manager") @Valid final ManagerDTO managerDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "manager/edit";
        }
        managerService.update(id, managerDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("manager.update.success"));
        return "redirect:/managers";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = managerService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            managerService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("manager.delete.success"));
        }
        return "redirect:/managers";
    }

}
