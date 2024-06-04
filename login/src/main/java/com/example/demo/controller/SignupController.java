package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.demo.form.SignupForm;
import com.example.demo.service.UserService;

import jakarta.validation.Valid;

@Controller
public class SignupController {

	private final UserService userService;

	public SignupController(UserService userService) {
		this.userService = userService;
	}

	@GetMapping("/admin/signup")
	public String showSignupForm(Model model) {
		model.addAttribute("signupForm", new SignupForm());
		return "signup";
	}

	@PostMapping("/admin/signup")
	public String signup(@Valid SignupForm form, BindingResult result, RedirectAttributes redirectAttributes,
			Model model) {
		if (result.hasErrors()) {
			model.addAttribute("signupForm", form);
			return "signup";
		}

		String message = userService.addUser(form);
		if ("User added successfully".equals(message)) {
			redirectAttributes.addFlashAttribute("successMessage", "ユーザーが正常に追加されました。");
			return "redirect:/admin/signup";
		} else {
			redirectAttributes.addFlashAttribute("errorMessage", "記入誤りがあります。確認してください。");
			return "redirect:/admin/signup";
		}
	}
}
