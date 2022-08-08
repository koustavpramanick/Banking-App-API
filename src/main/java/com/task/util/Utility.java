package com.task.util;

import com.task.exception.InsufficientBalanceException;

public class Utility {

	public static boolean validateInitialDeposit(Double accountOpeningBalance) {
		if (accountOpeningBalance<10000d) {
			throw new InsufficientBalanceException("Account opening balance needs to be greater than 10000!!");
		}else {
			return true;
		}
	}
	
	public static Float getInterestRateForAccountType(String accountType) {
		if(accountType.equalsIgnoreCase("SAVINGS")) {
			return 3.00f;
		}else {
			return 2.00f;
		}
	}
}
