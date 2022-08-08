package com.task.dto;

import com.task.model.Account;
import com.task.model.Branch;
import com.task.model.Customer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccountCreationAcknowledgement {

	private String status;
	private Customer customer;
}
