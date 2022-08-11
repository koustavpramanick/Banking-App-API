# Banking-App-API
A banking application using Spring boot and REST API, Using MySQL for Storage , MongoDB for EventStore and Redis for cacheing

# Prerequisite
1. MySQL Server, MongoDB Server, Redis Server, jdk 8 or above
2. Docker compose file is present is project file
3. Postman for API testing (Optional)

# Problem Statement:
The project is mainly based on the idea of developing a banking solution on micro services API based solution, which would replace the existing manual banking solutions. All the operations that are carried out in the bank manually would be performed automatically and easily by the Core Banking Solution.

Use cases:

Create customer (with Account using atomic behaviour)
Update customer
Get customer (along with account details)
Get all customer
Delete customer (Soft Delete)
Create account
Update account
Get account
Get all account
Close/Delete Account (Soft Delete)
Cache the result from get customer, account, update customer, account
Evict from cache when a customer or account is deleted
Update cache when customer or account details updated
Store customer and account details in MySql db
Store all the events related to customer and account in the mongo db. Create different events according to the use cases.
When a customer is created an account is also created. Make sure it is Atomic.(Using transaction management)
Initially balance can be set to minimum of Rs 10000 else account will not be created
Models:
Customer name, customer id, account id, age, mobile, address, status, created date, updated date

Account account id, type, status, branch opening date, updated date, currentBalance, interestRate

Branch branch id, address, phone

# Request Bodies
Create Account For Customer:
{
    "customerInfo": {
        "name": "User One",
        "age": 18,
        "mobile": "12345678",
        "address": "Place One"
    },
    "accountInfo": {
        "type": "SAVINGS",
        "branch": {
            "address": "Place One",
            "phone": "1234567890"
        },
        "balance": 200000
    }
}

Create Account for existing Customer:
{
	"customerId":22,
	"type":"CURRENT",
	"branch":{
		"address":"Place Two",
		"phone":"1111111111"
	},
	"balance": 500000
}

Update Customer:
{
    "name": "User Two",
    "age": 38,
    "mobile": "9988776655",
    "address": "Place Three"
}

Update Account:
{
    "type": "SAVINGS",
    "branch": {
        "address": "Place Four",
        "phone": "4444444444"
    },
    "balance": 5000000
}
