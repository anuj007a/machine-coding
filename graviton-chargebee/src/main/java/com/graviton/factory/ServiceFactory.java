package com.graviton.factory;

import com.graviton.repository.PricingRepository;
import com.graviton.repository.CustomerRepository;
import com.graviton.repository.TransactionRepository;
import com.graviton.repository.impl.PricingRepositoryImpl;
import com.graviton.repository.impl.CustomerRepositoryImpl;
import com.graviton.repository.impl.TransactionRepositoryImpl;
import com.graviton.service.TransactionService;
import com.graviton.service.PricingService;
import com.graviton.service.impl.TransactionServiceImpl;
import com.graviton.service.impl.PricingServiceImpl;

/**
 * ServiceFactory is a factory class responsible for creating and providing
 * instances of service implementations. It handles the instantiation of
 * repository and service classes, ensuring that services are properly
 * configured with the necessary dependencies.
 */
public class ServiceFactory {

    // Static instances of repository implementations to be used by the services.
    private static final PricingRepository pricingRepository = new PricingRepositoryImpl();
    private static final CustomerRepository customerRepository = new CustomerRepositoryImpl();
    private static final TransactionRepository transactionRepository = new TransactionRepositoryImpl();

    /**
     * Provides an instance of PricingService with the required dependencies.
     *
     * @return An instance of PricingServiceImpl configured with PricingRepository
     *         and CustomerRepository.
     */
    public static PricingService getPricingService() {
        return new PricingServiceImpl(pricingRepository, customerRepository);
    }

    /**
     * Provides an instance of TransactionService with the required dependencies.
     *
     * @return An instance of TransactionServiceImpl configured with TransactionRepository,
     *         PricingRepository, and CustomerRepository.
     */
    public static TransactionService getTransactionService() {
        return new TransactionServiceImpl(transactionRepository, pricingRepository, customerRepository);
    }
}
