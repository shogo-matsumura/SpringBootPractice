package com.example.demo.service;

import com.example.demo.form.SignupForm;

public interface UserService {
    String authenticateUser(String email, String password);
    String addUser(SignupForm form);
}
