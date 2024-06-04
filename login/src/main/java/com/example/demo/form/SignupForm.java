package com.example.demo.form;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupForm {

	@NotBlank(message = "姓は必須です")
	private String lastName;

	@NotBlank(message = "名は必須です")
	private String firstName;

	@NotBlank(message = "メールアドレスは必須です")
	private String email;

	@NotBlank(message = "パスワードは必須です")
	private String password;
}
