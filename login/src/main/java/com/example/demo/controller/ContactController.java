package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.entity.Contact;
import com.example.demo.form.ContactsForm;
import com.example.demo.repository.ContactRepository;
import com.example.demo.service.ContactService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class ContactController {

	private final ContactRepository contactRepository;
	private final ContactService contactService;

	public ContactController(ContactRepository contactRepository, ContactService contactService) {
		this.contactRepository = contactRepository;
		this.contactService = contactService;
	}

	@GetMapping("/contacts")
	public String view(Model model) {
		List<Contact> contacts = contactRepository.findAll();
		model.addAttribute("contacts", contacts);
		return "contactList";
	}

	//お問い合わせ詳細画面
	@GetMapping("/contacts/{id}")
	public String detail(@PathVariable("id") Long id, Model model) {
		return contactService.getContactDetail(id, model);
	}

	//お問い合わせ編集画面
	@GetMapping("/contacts/{id}/edit")
	public String edit(@PathVariable Long id, Model model) {
		return contactService.edit(id, model);
	}

	@PostMapping("/contacts/{id}")
	public String update(@PathVariable Long id, @ModelAttribute ContactsForm form) {
		return contactService.update(id, form);
	}

	// 削除ボタン後のコントローラ
	@PostMapping("/contacts/delete")
	public String delete(@RequestParam("id") Long id) {
		contactService.delete(id);
		return "redirect:/admin/contacts"; // 削除後に /admin/contacts にリダイレクト
	}

	// お問い合わせ内容確認、表示
	@GetMapping("/contactForm")
	public String contactForm(Model model) {
		model.addAttribute("contactForm", new ContactsForm());
		return "contactForm";
	}

	@PostMapping("/contactForm")
	public String confirmation(@ModelAttribute("contactForm") @Validated ContactsForm contactForm,
			BindingResult errorResult, HttpServletRequest request) {
		if (errorResult.hasErrors()) {
			return "contactForm";
		}
		HttpSession session = request.getSession();
		session.setAttribute("contactForm", contactForm);
		return "confirmation";
	}

	@PostMapping("/confirmation")
	public String confirm(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		ContactsForm contactsForm = (ContactsForm) session.getAttribute("contactForm"); // 修正: 正しいクラス名を使用

		if (contactsForm != null) {
			Contact contact = new Contact();
			contact.setLastName(contactsForm.getLastName());
			contact.setFirstName(contactsForm.getFirstName());
			contact.setEmail(contactsForm.getEmail());
			contact.setPhone(contactsForm.getPhone());
			contact.setZipCode(contactsForm.getZipCode());
			contact.setAddress(contactsForm.getAddress());
			contact.setBuildingName(contactsForm.getBuildingName());
			contact.setContactType(contactsForm.getContactType());
			contact.setBody(contactsForm.getBody());

			contactRepository.save(contact);
		}

		return "redirect:/admin/complete";
	}

	@GetMapping("/complete")
	public String complete(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if (session == null || session.getAttribute("contactForm") == null) {
			return "redirect:/contact";
		}

		ContactsForm contactForm = (ContactsForm) session.getAttribute("contactForm");
		model.addAttribute("contactForm", contactForm);

		return "completion";
	}

	//ログアウト
	@GetMapping("/admin/logout")
	public String logout() {
		return "redirect:/admin/signin";
	}
}
