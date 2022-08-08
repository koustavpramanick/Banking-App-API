package com.task.service;

import com.task.dto.AccountCreationAcknowledgement;
import com.task.dto.AccountCreationRequest;
import com.task.exception.AccountNotFoundException;
import com.task.exception.CustomerNotFoundException;
import com.task.model.Account;
import com.task.model.Customer;
import com.task.model.EventLog;
import com.task.model.Status;
import com.task.repository.*;
import com.task.util.Utility;

import antlr.Utils;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class BankService {

	@Autowired
	AccountRepository accountRepo;
	
	@Autowired
	CustomerRepository customerRepo;
	
	@Autowired
	EventLogRepository eventRepo;
	
	@Transactional
	public AccountCreationAcknowledgement createAccountForCustomer (AccountCreationRequest request) {
		
		Customer customerInfo = request.getCustomerInfo();
		Account accountInfo = request.getAccountInfo();
		//This Method will throw exception on lower limit
		Utility.validateInitialDeposit(accountInfo.getBalance());
		
		customerInfo.setCreatedDate(new Date());
		customerInfo.setUpdateddate(new Date());
		customerInfo.setStatus(Status.CREATED);
		customerInfo = customerRepo.save(customerInfo);
		
		
		
		//This Method will throw exception on lower limit
		//Utility.validateInitialDeposit(accountInfo.getBalance());
		
		accountInfo.setInterestRate(Utility.getInterestRateForAccountType(accountInfo.getType()));
		accountInfo.setCustomerId(customerInfo.getCustomerId());
		accountInfo.setStatus(Status.CREATED);
		accountRepo.save(accountInfo);
		
		//eventRepo.save(new EventLog("Account Created for : " + customerInfo.getName()+" Account Number : "+ accountInfo.getAccountId()));
		return new AccountCreationAcknowledgement("ACCOUNT_CREATED",customerInfo);
	
	}
	
	public AccountCreationRequest createAccountForExistingCustomer(Account accountDetails) {
		Customer customer = customerRepo.findById(accountDetails.getCustomerId()).get();
		if(customer != null) {
			Utility.validateInitialDeposit(accountDetails.getBalance());
			Account account = new Account();
			account.setCustomerId(accountDetails.getCustomerId());
			account.setType(accountDetails.getType());
			account.setStatus(Status.CREATED);
			account.setBalance(accountDetails.getBalance());
			account.setBranch(accountDetails.getBranch());
			account.setInterestRate(Utility.getInterestRateForAccountType(accountDetails.getType()));
			accountRepo.save(account);
			return new AccountCreationRequest(customer, account);
		}else {
			throw new CustomerNotFoundException("No customer present for given id: "+accountDetails.getCustomerId());
		}
	}

	public List<Customer> fetchAllCustomer() {	
		eventRepo.save(new EventLog("All Customers are Fetched"));
		return customerRepo.findAll();
	}

	 @Cacheable(value = "customer", key = "#id")
	 public Customer fetchCustomerByID(int id) {
		return customerRepo.findById(id).isPresent()?customerRepo.findById(id).get():null;
	}

	@CacheEvict(value = "customer", key = "#id")
	public Customer deleteCustomer(int id) {
		Customer customer = customerRepo.findById(id).get();
		if (customer  != null && customer.getStatus()!=Status.DELETED) {
			customer.setStatus(Status.DELETED);
			//when customer deleted accounts needs to be closed
			//List<Account> accountList = accountRepo.findAccountForCustomerId(id);
			accountRepo.findAccountForCustomerId(id).forEach(acc -> {
				acc.setStatus(Status.CLOSED);
				accountRepo.save(acc);
				eventRepo.save(new EventLog("Account "+ acc.getAccountId()+" Closed"));
			});
			eventRepo.save(new EventLog("Customer "+ customer.getCustomerId()+" Deleted"));
			return customerRepo.save(customer);
		} else {
			eventRepo.save(new EventLog("No Customer Present to Delete for Id: " + id));
			return null;
		}
	}

	@CachePut(value = "customer", key = "#id")
	public Customer updateCustomerDetails(int id, Customer customerDetails) {
		return customerRepo.findById(id).map(cust -> {
			cust.setAge(customerDetails.getAge());
			cust.setName(customerDetails.getName());
			cust.setMobile(cust.getMobile());
			cust.setAddress(customerDetails.getAddress());
			cust.setUpdateddate(new Date());
			customerRepo.save(cust);
			eventRepo.save(new EventLog("Customer Updated for Id: " + id));
			return cust;
		}).orElseThrow(() -> {
			eventRepo.save(new EventLog("Customer Updation failed for Id: " + id));
			throw new CustomerNotFoundException("Updation fail for Customer id: "+id);
				});			
	}

	public List<Account> fetchAllAccount() {
		eventRepo.save(new EventLog("All Accounts are Fetched"));
		return accountRepo.findAll();
	}

	@Cacheable(value = "account", key = "#id")
	public Account fetchAccountById(int id) {
		return accountRepo.findById(id).isPresent()?accountRepo.findById(id).get():null;
	}

	@CachePut(value = "account", key = "#id")
	public Account updateAccountDetails(int id, Account accountDetails) {
		return accountRepo.findById(id).map(acc -> {
			acc.setBalance(accountDetails.getBalance());
			acc.setBranch(accountDetails.getBranch());
			acc.setType(accountDetails.getType());
			acc.setInterestRate(Utility.getInterestRateForAccountType(accountDetails.getType()));
			eventRepo.save(new EventLog("Account Updated for Id: " + id));
			accountRepo.save(acc);
			return acc;
		}).orElseThrow(()->{
			eventRepo.save(new EventLog("Account updation failed for id: "+ id));
			throw new AccountNotFoundException("Account updation failed for id: "+ id);
		});
	}

	@CacheEvict(value = "account", key = "#id")
	public Account deleteAccount(int id) {
		Account account = accountRepo.findById(id).get();
		if (account  != null && account.getStatus()!=Status.DELETED) {
			account.setStatus(Status.DELETED);
			eventRepo.save(new EventLog("Customer "+ account.getAccountId()+" Deleted"));
			return accountRepo.save(account);
		} else {
			eventRepo.save(new EventLog("No Customer Present to Delete for Id: " + id));
			return null;
		}
	}

	



}
