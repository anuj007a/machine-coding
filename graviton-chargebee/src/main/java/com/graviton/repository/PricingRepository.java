package com.graviton.repository;

import com.graviton.model.PackageDetails;
import com.graviton.model.Service;

public interface PricingRepository {
    PackageDetails getPackage(String packageName);

    void addPackage(PackageDetails Package);

    Service getService(String serviceName);

    void addService(Service service);
}
