
# Question

## Graviton Command-Line Application
### Objective
This application processes three input JSON files to manage customer purchases, service usage, and pricing information. It produces an output showing the transaction history and customer credit balances.

### Requirements
1. **Input Files**:
    - **Pricing Information**: Contains service pricing and credit packages.
    - **Purchase Information**: Contains customer package purchases.
    - **Usage Information**: Contains customer service usage.
2. **Service Pricing**:
    - S1 requires 1 credit per usage.
    - S2 requires 2 credits per usage.
    - S3 requires 3 credits per usage.
3. **Credit Packages**:
    - Basic Package: 100 credits for $100.00
    - Standard Package: 250 credits for $225.00
    - Premium Package: 500 credits for $450.00
### -----------------------------------------------------------------------------------------
### Setup
#### Prerequisites
* Java 11 or later
* Maven 3.6.3 or later

#### Compile the Project
To compile the project, run the following command in the project root directory:
```sh
mvn clean compile
```
## Usage
### Running the Test Cases
To run the unit tests, use the following command:
```sh
   mvn test
```
### Running the Application
To run the application, use the following command:
```sh
   mvn clean install
   mvn exec:java -Dexec.mainClass="com.graviton.driver.Main"
```
## Example JSON Files

**pricing.json:**  Contains service pricing and credit packages.
```json
{
  "services": [
    {"serviceName": "S1", "creditCost": 1},
    {"serviceName": "S2", "creditCost": 2},
    {"serviceName": "S3", "creditCost": 3}
  ],
  "packages": [
    {"name": "Basic", "credits": 100, "price": 100.00},
    {"name": "Standard", "credits": 250, "price": 225.00},
    {"name": "Premium", "credits": 500, "price": 450.00}
  ]
}
```
**purchase.json:** Contains customer purchase information.
```json
{
   "purchase": [
      {"customerId": "C1", "packageName": "Basic"},
      {"customerId": "C2", "packageName": "Standard"},
      {"customerId": "C1", "packageName": "Basic"}
   ]
}
```

**usages.json:** Contains customer usage information.
```json
{
   "usages": [
      {"customerId": "C1", "serviceName": "S1"},
      {"customerId": "C1", "serviceName": "S2"},
      {"customerId": "C1", "serviceName": "S3"},
      {"customerId": "C1", "serviceName": "S2"},
      {"customerId": "C2", "serviceName": "S1"}
   ]
}
```

## Folder structure

```sh
├graviton-chargebee/
├── approach.md
├── pom.xml
├── README.md
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │      └── graviton/
    │   │          ├── model/
    │   │          │   ├── Customer.java
    │   │          │   ├── PackageDetails.java
    │   │          │   ├── Purchase.java
    │   │          │   ├── Service.java
    │   │          │   ├── Transaction.java
    │   │          │   └── Usage.java
    │   │          ├── repository/
    │   │          │   ├── CustomerRepository.java
    │   │          │   ├── PricingRepository.java
    │   │          │   ├── TransactionRepository.java
    │   │          │   └── impl/
    │   │          │       ├── CustomerRepositoryImpl.java
    │   │          │       ├── PricingRepositoryImpl.java
    │   │          │       └── TransactionRepositoryImpl.java
    │   │          ├── service/
    │   │          │   ├── PricingService.java
    │   │          │   ├── TransactionService.java
    │   │          │   └── impl/
    │   │          │       ├── PricingServiceImpl.java
    │   │          │       └── TransactionServiceImpl.java
    │   │          ├── factory/
    │   │          │   └── ServiceFactory.java
    │   │          ├── util/
    │   │          │   └── FileReader.java
    │   │          ├── driver/
    │   │          │   └── Main.java
    │   │          ├── enums/
    │   │          │   └── TransactionType.java
    │   │          └── exception/
    │   │              └── GravitonException.Java
    │   └── resources/
    │       ├── pricing.json
    │       ├── purchase.json
    │       └── usages.json
    │
    └── test/
        └── java/
           └── com/
               └── graviton/
                   ├── repository/
                   │   ├── CustomerRepositoryImplTest.java
                   │   ├── PricingRepositoryImplTest.java
                   │   └── TransactionRepositoryImplTest.java
                   └── service/
                       ├── PricingServiceImplTest.java
                       └── TransactionServiceImplTest.java
```