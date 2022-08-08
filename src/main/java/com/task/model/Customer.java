package com.task.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.redis.core.RedisHash;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Customers")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer implements Serializable{
	
	   private static final long serialVersionUID = -4878914989062566641L;
	   @Id
	   @GeneratedValue(strategy = GenerationType.AUTO)
	   private int customerId;  
	   private String name;
	   //@OneToMany(cascade = CascadeType.ALL)
	   //@JoinTable(name = "customer_account",joinColumns = { @JoinColumn(name = "customerId")},inverseJoinColumns = {@JoinColumn(name = "accountId")})
	   //private List<Account> accounts;
	   private Integer age;
	   private String mobile;
	   private String address;
	   @Enumerated(EnumType.STRING)
	   private Status status;
	   private Date createdDate;
	   private Date updateddate;
}
