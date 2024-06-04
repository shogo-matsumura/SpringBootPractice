package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.form.SigninForm;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@Controller
public class SigninController {

    private final UserService userService;

    public SigninController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/admin/signin")
    public String showsigninForm(Model model) {
        model.addAttribute("signinForm", new SigninForm());
        return "signin";
    }

    @PostMapping("/admin/contacts")
    public String signin(@Valid SigninForm form, BindingResult result, RedirectAttributes redirectAttributes, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("signinForm", form);
            return "signin";
        }

        String email = form.getEmail();
        String password = form.getPassword();
        String message = userService.authenticateUser(email, password);
        if ("Authenticated User".equals(message)) {
            redirectAttributes.addFlashAttribute("successMessage", "Logged in successfully!");
            return "redirect:/admin/contacts";
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", message);
            return "redirect:/admin/signin";
        }
    }
}
