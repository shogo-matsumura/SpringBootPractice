package com.example.demo.service;

import org.springframework.ui.Model;

import com.example.demo.entity.Contact;
import com.example.demo.form.ContactsForm;

public interface ContactService {
    String getContactDetail(Long id, Model model);
    String edit(Long id, Model model);
    String update(Long id, ContactsForm form);
    Contact getById(Long id);
    void delete(Long id);
}
