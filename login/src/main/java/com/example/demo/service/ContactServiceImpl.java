package com.example.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import com.example.demo.entity.Contact;
import com.example.demo.form.ContactsForm;
import com.example.demo.repository.ContactRepository;

    @Service
    public class ContactServiceImpl implements ContactService {

    private final ContactRepository contactRepository;

    public ContactServiceImpl(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }
    @Override
    public String getContactDetail(Long id, Model model) {
        Optional<Contact> contactOptional = contactRepository.findById(id);
        if (contactOptional.isPresent()) {
            Contact contact = contactOptional.get();
            model.addAttribute("contact", contact);
            return "contactsid";
        } else {
            return "error_page";
        }
    }
    @Override
    public String edit(Long id, Model model) {
        Optional<Contact> contactOptional = contactRepository.findById(id);
        if (contactOptional.isPresent()) {
            Contact contact = contactOptional.get();
            model.addAttribute("contact", contact);
            return "contactsedit";
        } else {
            return "error";
        }
    }
    @Override
    public String update(Long id, ContactsForm form) {
        Optional<Contact> optionalContact = contactRepository.findById(id);
        Contact contact = optionalContact.orElseThrow(() -> new RuntimeException("お問い合わせが見つかりません: " + id));

        contact.setLastName(form.getLastName());
        contact.setFirstName(form.getFirstName());
        contact.setEmail(form.getEmail());
        contact.setPhone(form.getPhone());
        contact.setZipCode(form.getZipCode());
        contact.setAddress(form.getAddress());
        contact.setBuildingName(form.getBuildingName());
        contact.setContactType(form.getContactType());
        contact.setBody(form.getBody());

        contactRepository.save(contact);

        return "redirect:/admin/contacts/" + id;
    }
    @Override
    public Contact getById(Long id) {
        return contactRepository.findById(id).orElse(null);
    }
    @Override
    public void delete(Long id) {
        contactRepository.deleteById(id);
    }
}
