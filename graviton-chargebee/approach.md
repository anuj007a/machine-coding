# Thought Process for Graviton Chargebee Implementation

## Problem Understanding

### Objective
The goal was to develop a command-line application that takes three input files representing pricing information, purchase information, and usage information, then produces an output file showing the transaction history for each customer along with their available credit balances.
### Key Requirements
1. **Credit-Based Billing**: Implement a credit-based billing system for three services (S1, S2, and S3).
2. **Credit Packages**: Manage different credit packages that customers can purchase.
3. **Transaction Management**: Track customer purchases and service usage, ensuring accurate credit balance updates.
4. **Error Handling**: Properly handle cases such as insufficient credits and non-existent customers or packages.

## Initial Approach

1. **Requirement Analysis**: Thoroughly analyzed the requirements to understand the scope and constraints.
2. **Design the Model**: Defined the key entities and their relationships:
    - **Customer**: Holds the customer's ID and credit balance.
    - **PackageDetails**: Represents the details of a credit package.
    - **Service**: Represents a service with a name and credit cost.
    - **Transaction**: Records a transaction (purchase or usage) for a customer.
    - **Usage**: Represents a service usage by a customer.
    - **Purchase**: Represents a purchase input of customers.

## Design Decisions

### Modular Design
Adopted a modular design pattern to separate concerns and make the codebase extensible and maintainable. Divided the code into different packages:
- **model**: Contains data classes.
- **repository**: Handles data storage and retrieval.
- **service**: Contains business logic.
- **factory**: Provides instances of services.
- **exception**: Custom exceptions.
- **util**: Utility classes for file reading.
- **driver**: Entry point of the application.
- **enums**: Contains enum.

### Repositories
Implemented repositories for managing the in-memory storage of customers, packages, services, and transactions:
- **CustomerRepository**: Manages customer data.
- **PricingRepository**: Manages package and service data.
- **TransactionRepository**: Manages transaction data.

### Services
Defined services to encapsulate business logic:
- **PricingService**: Handles operations related to packages and services.
- **TransactionService**: Manages customer purchases and service usage.

### Factory Pattern
Used the factory pattern to instantiate service objects, ensuring loose coupling between components.

### Exception Handling
Implemented custom exceptions for various error scenarios, such as customer not found, package not found, service not found, and insufficient credits. This provides clear error messages and helps in debugging.

## Implementation Steps

1. **Set Up Project Structure**: Organized the project directories and packages.
2. **Define Models**: Created the necessary data classes with appropriate constructors, getters, and setters.
3. **Implement Repositories**: Developed in-memory repositories for storing and retrieving data.
4. **Create Services**: Implemented business logic in service classes.
5. **Develop Utility Classes**: Wrote a utility class for reading JSON input files and mapping them to Java objects.
6. **Write Main Class**: Developed the main class to tie everything together and handle the program flow.
7. **Unit Testing**: Wrote unit tests for repositories and services to ensure correctness.