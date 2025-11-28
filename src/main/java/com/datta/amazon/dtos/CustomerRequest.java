package com.datta.amazon.dtos;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CustomerRequest {
	@NotNull
	private String email;
	@NotNull
	private String name;
	@NotNull
	private String password;
}
