package com.example.demo.service;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Admin;
import com.example.demo.form.SignupForm;
import com.example.demo.repository.AdminRepository;

import jakarta.transaction.Transactional;

    @Service
    @Transactional
    public class UserServiceImpl implements UserService {

	private final AdminRepository repository;
	private final PasswordEncoder passwordEncoder;

	public UserServiceImpl(AdminRepository repository, PasswordEncoder passwordEncoder) {
		this.repository = repository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public String authenticateUser(String email, String password) {
		Optional<Admin> adminOptional = repository.findByEmail(email);
		if (adminOptional.isPresent()) {
			Admin admin = adminOptional.get();
			if (passwordEncoder.matches(password, admin.getPassword())) {
				return "Authenticated User";
			} else {
				return "Incorrect password";
			}
		} else {
			return "User not found";
		}
	}

	@Override
	public String addUser(SignupForm form) {
		Optional<Admin> adminOptionalExistedOpt = repository.findByEmail(form.getEmail());
		if (adminOptionalExistedOpt.isPresent()) {
			return "User already exists";
		}

		Admin admin = new Admin();
		admin.setLastName(form.getLastName());
		admin.setFirstName(form.getFirstName());
		admin.setEmail(form.getEmail());
		admin.setPassword(passwordEncoder.encode(form.getPassword()));

		repository.save(admin);
		return "User added successfully";
	}
}
