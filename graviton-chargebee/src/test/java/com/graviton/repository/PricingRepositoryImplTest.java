package com.graviton.repository;

import com.graviton.model.PackageDetails;
import com.graviton.model.Service;
import com.graviton.repository.impl.PricingRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PricingRepositoryImplTest {

    private PricingRepositoryImpl pricingRepository;

    @BeforeEach
    void setUp() {
        pricingRepository = new PricingRepositoryImpl();
    }

    @Test
    void testAddPackage_Success() {
        PackageDetails packageDetails = new PackageDetails("Basic", 100, 100.00);

        assertDoesNotThrow(() -> pricingRepository.addPackage(packageDetails));
    }

    @Test
    void testAddPackage_NullPackageDetails() {
        assertThrows(IllegalArgumentException.class, () -> pricingRepository.addPackage(null));
    }

    @Test
    void testAddPackage_NullPackageName() {
        PackageDetails packageDetails = new PackageDetails(null, 100, 100.00);
        assertThrows(IllegalArgumentException.class, () -> pricingRepository.addPackage(packageDetails));
    }

    @Test
    void testGetPackage_Success() {
        PackageDetails packageDetails = new PackageDetails("Basic", 100, 100.00);
        pricingRepository.addPackage(packageDetails);

        PackageDetails retrievedPackage = pricingRepository.getPackage("Basic");
        assertNotNull(retrievedPackage);
        assertEquals("Basic", retrievedPackage.getName());
    }

    @Test
    void testGetPackage_PackageNotFound() {
        assertThrows(IllegalArgumentException.class, () -> pricingRepository.getPackage("NonExistentPackage"));
    }

    @Test
    void testGetPackage_NullPackageName() {
        assertThrows(IllegalArgumentException.class, () -> pricingRepository.getPackage(null));
    }

    @Test
    void testAddService_Success() {
        Service service = new Service("S1", 10.0);

        assertDoesNotThrow(() -> pricingRepository.addService(service));
    }

    @Test
    void testAddService_NullService() {
        assertThrows(IllegalArgumentException.class, () -> pricingRepository.addService(null));
    }

    @Test
    void testAddService_NullServiceName() {
        Service service = new Service(null, 10.0);
        assertThrows(IllegalArgumentException.class, () -> pricingRepository.addService(service));
    }

    @Test
    void testGetService_Success() {
        Service service = new Service("S1", 10.0);
        pricingRepository.addService(service);

        Service retrievedService = pricingRepository.getService("S1");
        assertNotNull(retrievedService);
        assertEquals("S1", retrievedService.getServiceName());
    }

    @Test
    void testGetService_ServiceNotFound() {
        assertThrows(IllegalArgumentException.class, () -> pricingRepository.getService("NonExistentService"));
    }

    @Test
    void testGetService_NullServiceName() {
        assertThrows(IllegalArgumentException.class, () -> pricingRepository.getService(null));
    }
}
