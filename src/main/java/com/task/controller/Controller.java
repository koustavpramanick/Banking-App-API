package com.task.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.task.dto.AccountCreationAcknowledgement;
import com.task.dto.AccountCreationRequest;
import com.task.exception.CustomerNotFoundException;
import com.task.exception.InsufficientBalanceException;
import com.task.model.Account;
import com.task.model.Customer;
import com.task.model.EventLog;
import com.task.repository.EventLogRepository;
import com.task.service.BankService;

@RestController
@EnableTransactionManagement
public class Controller {
	
	@Autowired
	EventLogRepository eventRepo;
	
	@Autowired
	BankService bankService;
	
    @PostMapping("/createAccountForCustomer")
    public AccountCreationAcknowledgement createAccForCust(@RequestBody AccountCreationRequest request) {
    	 	
    	try {
    		AccountCreationAcknowledgement acknowledgement = bankService.createAccountForCustomer(request);
    		eventRepo.save(new EventLog("Account Created for : " + request.getCustomerInfo().getName()+" Account Number : "+ request.getAccountInfo().getAccountId()));
    		return acknowledgement;
		} catch (InsufficientBalanceException e) {
			e.printStackTrace();
			eventRepo.save(new EventLog("Minimum Balance is not Diposited for :" + request.getCustomerInfo().getName()));
			return new AccountCreationAcknowledgement("INSUFFICIENT_BALANCE_EXCEPTION:CHECK_EVENTLOG",request.getCustomerInfo());
		}catch (Exception e) {
			e.printStackTrace();
			eventRepo.save(new EventLog("Exception Occured Check EventLog " +'\n'+ e.getMessage()));
			return new AccountCreationAcknowledgement("EXCEPTION_OCCURED:CHECK_EVENTLOG",request.getCustomerInfo());
		}
    	  	
    }
    
    @PostMapping("/createAccForExistingCustomer")
    public ResponseEntity<AccountCreationRequest> createAccForExistingCust(@RequestBody Account accountDetails) {
    	try {
    		AccountCreationRequest ack = bankService.createAccountForExistingCustomer(accountDetails);
    		eventRepo.save(new EventLog("Account Created for Existing Customer: " + ack.getCustomerInfo().getName()+" Account Number : "+ ack.getAccountInfo().getAccountId()));
    		return ResponseEntity.ok(ack);
		} catch (CustomerNotFoundException e) {
			e.printStackTrace();
			eventRepo.save(new EventLog("No Customer Present for Customer Id :" + accountDetails.getCustomerId()));
			return ResponseEntity.notFound().build();
		}catch (Exception e) {
			e.printStackTrace();
			eventRepo.save(new EventLog("Exception Occured Check EventLog " +'\n'+ e.getMessage()));
			return ResponseEntity.notFound().build();
		}
    }
    
    @GetMapping("/getAllCustomer")
    public ResponseEntity<List<Customer>> getAllCustomer(){
    	return ResponseEntity.ok(bankService.fetchAllCustomer());
    }
    
    //@Cacheable(value = "customer", key = "#id")//, condition = "#result != null")
    @GetMapping("/getCustomer/{id}")
    public ResponseEntity<Customer> getCustomerById(@PathVariable int id){
    	Customer customer = bankService.fetchCustomerByID(id);
    	if(customer != null) {
    		eventRepo.save(new EventLog("Customer Fetched with id: " +id));
    		return ResponseEntity.ok(customer);
    	}else {
    		eventRepo.save(new EventLog("No Customer present for id: " +id));
    		return ResponseEntity.notFound().build();
    	}
    		
    }
    
    //@CachePut(value = "customer", key = "cust + #id")
    @PutMapping("/updateCustomer/{id}")
    public ResponseEntity<Customer> updateCustomer(@PathVariable int id,@RequestBody Customer customerDetails ){
    	Customer updateCustomer = bankService.updateCustomerDetails(id,customerDetails);
    	if(updateCustomer!= null)
    		return ResponseEntity.ok(updateCustomer);
    	else
    		return ResponseEntity.notFound().build();
    	    	
    }
    
    //@CacheEvict(value = "customer", key = "cust + #id")
    @DeleteMapping("deleteCustomer/{id}")
    public ResponseEntity<Customer> deleteCustomer(@PathVariable int id){
    	Customer customer = bankService.deleteCustomer(id);
    	if(customer != null) {
    		return ResponseEntity.accepted().body(customer);
    	}else {
    		return ResponseEntity.notFound().build();
    	}
    }
    
    
    @GetMapping("/getAllAccount")
    public ResponseEntity<List<Account>> getAllAccount(){
    	return ResponseEntity.ok(bankService.fetchAllAccount());
    }
    
    //@Cacheable(value = "customer", key = "acc + #id", condition = "#result != null")
    @GetMapping("/getAccount/{id}")
    public ResponseEntity<Account> getAccountById(@PathVariable int id){
    	Account acc = bankService.fetchAccountById(id);
    	if(acc != null) {
    		eventRepo.save(new EventLog("Account Fetched with id: " +id));
    		return ResponseEntity.ok(acc);
    	}else {
    		eventRepo.save(new EventLog("No Account present for id: " +id));
    		return ResponseEntity.notFound().build();
    	}
    }
    
    //@CachePut(value = "customer", key = "acc + #id")
    @PutMapping("/updateAccount/{id}")
    public ResponseEntity<Account> updateAccount(@PathVariable int id,@RequestBody Account accountDetails ){
    	Account updateAccount = bankService.updateAccountDetails(id,accountDetails);
    	if(updateAccount!= null)
    		return ResponseEntity.ok(updateAccount);
    	else
    		return ResponseEntity.notFound().build();
    	    	
    }
    //@CacheEvict(value = "customer", key = "acc + #id")
    @DeleteMapping("deleteAccount/{id}")
    public ResponseEntity<Account> deleteAccount(@PathVariable int id){
    	Account account = bankService.deleteAccount(id);
    	if(account != null) {
    		return ResponseEntity.accepted().body(account);
    	}else {
    		return ResponseEntity.notFound().build();
    	}
    }
}
    
