package io.bootify.spring_car_rental.mvc_controller;

import io.bootify.spring_car_rental.domain.CarRental;
import io.bootify.spring_car_rental.DTO.PaymentDTO;
import io.bootify.spring_car_rental.repos.CarRentalRepository;
import io.bootify.spring_car_rental.service.interf.PaymentService;
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
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;
    private final CarRentalRepository carRentalRepository;

    public PaymentController(final PaymentService paymentService,
            final CarRentalRepository carRentalRepository) {
        this.paymentService = paymentService;
        this.carRentalRepository = carRentalRepository;
    }

    @ModelAttribute
    public void prepareContext(final Model model) {
        model.addAttribute("paymentValues", carRentalRepository.findAll(Sort.by("id"))
                .stream()
                .collect(Collectors.toMap(CarRental::getId, CarRental::getId)));
    }

    @GetMapping
    public String list(final Model model) {
        model.addAttribute("payments", paymentService.findAll());
        return "payment/list";
    }

    @GetMapping("/add")
    public String add(@ModelAttribute("payment") final PaymentDTO paymentDTO) {
        return "payment/add";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("payment") @Valid final PaymentDTO paymentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "payment/add";
        }
        paymentService.create(paymentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("payment.create.success"));
        return "redirect:/payments";
    }

    @GetMapping("/add/{id}")
    public String addFromCarRental(final Model model, @PathVariable final Long id) {
        PaymentDTO paymentDTO = new PaymentDTO();
        paymentDTO.setCarRentalId(id);
        model.addAttribute("payment", paymentDTO);
        return "payment/add_from_car_rental";
    }

    @PostMapping("/add/{id}")
    public String addFromCarRental(@ModelAttribute("payment") @Valid final PaymentDTO paymentDTO,
                      final BindingResult bindingResult,
                      final RedirectAttributes redirectAttributes,
                      @PathVariable final Long id) {
        if (bindingResult.hasErrors()) {
            System.out.println("bindingResult.hasErrors() = " + bindingResult.getAllErrors());
            return "payment/add_from_car_rental";
        }
        System.out.println("paymentDTO1 = " + paymentDTO);
        paymentDTO.setCarRentalId(id);
        System.out.println("paymentDTO2 = " + paymentDTO);
        paymentService.create(paymentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("payment.create.success"));
        return "redirect:/payments";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable final Long id, final Model model) {
        model.addAttribute("payment", paymentService.get(id));
        return "payment/edit";
    }

    @PostMapping("/edit/{id}")
    public String edit(@PathVariable final Long id,
            @ModelAttribute("payment") @Valid final PaymentDTO paymentDTO,
            final BindingResult bindingResult, final RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return "payment/edit";
        }
        paymentService.update(id, paymentDTO);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_SUCCESS, WebUtils.getMessage("payment.update.success"));
        return "redirect:/payments";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable final Long id, final RedirectAttributes redirectAttributes) {
        paymentService.delete(id);
        redirectAttributes.addFlashAttribute(WebUtils.MSG_INFO, WebUtils.getMessage("payment.delete.success"));
        return "redirect:/payments";
    }

}
