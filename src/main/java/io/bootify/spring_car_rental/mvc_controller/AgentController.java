package io.bootify.spring_car_rental.mvc_controller;

import io.bootify.spring_car_rental.domain.Agency;
import io.bootify.spring_car_rental.DTO.user_management.AgentDTO;
import io.bootify.spring_car_rental.repos.AgencyRepository;
import io.bootify.spring_car_rental.service.interf.AgentService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/agents")
public class AgentController {

    private final AgentService agentService;
    private final AgencyRepository agencyRepository;

    public AgentController(final AgentService agentService,
            final AgencyRepository agencyRepository) {
        this.agentService = agentService;
        this.agencyRepository = agencyRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("agencyValues", agencyRepository.findAll(Sort.by("id"))
                .stream()
                .collect(Collectors.toMap(Agency::getId, Agency::getName)));
    }

    @GetMapping
    public String list(@RequestParam(required = false) final String filter, final Model model) {
        model.addAttribute("agents", agentService.findAll(filter));
        model.addAttribute("filter", filter);
        return "agent/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("agent") final AgentDTO agentDTO) {
        return "agent/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("agent") @Valid final AgentDTO agentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "agent/add";
        }
        agentService.create(agentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("agent.create.success"));
        return "redirect:/agents";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("agent", agentService.get(id));
        return "agent/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("agent") @Valid final AgentDTO agentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "agent/edit";
        }
        agentService.update(id, agentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("agent.update.success"));
        return "redirect:/agents";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        final String referencedWarning = agentService.getReferencedWarning(id);
        if (referencedWarning != null) {
            redirectAttributes.addFlashAttribute(WebUtils.MSG_ERROR, referencedWarning);
        } else {
            agentService.delete(id);
            redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("agent.delete.success"));
        }
        return "redirect:/agents";
    }

}
