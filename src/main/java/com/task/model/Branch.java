package com.task.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "Branches")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Branch implements Serializable{

	private static final long serialVersionUID = -5288425580067353823L;
	@Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private int branchId;
	 private String address;
	 private String phone;
}
