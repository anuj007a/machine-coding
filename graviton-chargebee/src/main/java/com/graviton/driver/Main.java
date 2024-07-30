package com.graviton.driver;

import com.graviton.factory.ServiceFactory;
import com.graviton.model.*;
import com.graviton.service.PricingService;
import com.graviton.service.TransactionService;
import com.graviton.util.FileReader;

import java.io.IOException;
import java.util.List;

/**
 * The entry point of the Graviton application.
 * Initializes services, loads data from JSON files, performs transactions, and prints customer details.
 */
public class Main {
    public static final String PRICING_JSON = "pricing.json";
    public static final String PURCHASE_JSON = "purchase.json";
    public static final String USAGES_JSON = "usages.json";

    /**
     * Main method to run the application.
     *
     * @param args Command-line arguments (not used).
     * @throws IOException If an error occurs while reading files.
     */
    public static void main(String[] args) throws IOException {

        // Initialize services using the service factory
        PricingService pricingService = ServiceFactory.getPricingService();
        TransactionService transactionService = ServiceFactory.getTransactionService();

        // Load and add package details to the pricing service
        List<PackageDetails> packageDetails = FileReader.loadPackageData(PRICING_JSON);
        for (PackageDetails packageDetail : packageDetails) {
            pricingService.addPackage(packageDetail);
        }

        // Load and add service details to the pricing service
        List<Service> services = FileReader.loadServiceData(PRICING_JSON);
        for (Service service : services) {
            pricingService.addService(service);
        }

        // Create and add customers
        Customer customer1 = new Customer("C1");
        Customer customer2 = new Customer("C2");
        pricingService.addCustomer(customer1);
        pricingService.addCustomer(customer2);

        // Load and process package purchases
        List<Purchase> purchases = FileReader.loadPurchaseData(PURCHASE_JSON);
        for (Purchase purchase : purchases) {
            transactionService.purchasePackage(purchase.customerId(), purchase.packageName());
        }

        // Load and process service usages
        List<Usage> usages = FileReader.loadUsagesData(USAGES_JSON);
        for (Usage usage : usages) {
            transactionService.useService(usage.customerId(), usage.serviceName());
        }

        // Print transaction details for each customer
        List<Customer> customers = pricingService.getAllCustomer();

        for (Customer customer : customers) {
            System.out.println("======================> Customer details of " + customer.getId() + " <======================");
            System.out.println("Customer " + customer.getId() + " balance is : " + customer.getCreditBalance());

            // Retrieve and print transaction history for the customer
            List<Transaction> transactionList = transactionService.getCustomerTransactionsHistory(customer.getId());
            for (Transaction transaction : transactionList) {
                System.out.println(transaction.toString());
            }

            System.out.println("<========================================================================================>");
        }
    }
}
