package Repositories.Impl;

import Model.Provider;
import Repositories.ProviderRepo;

import java.util.HashMap;
import java.util.Map;

public class ProviderRepoImpl implements ProviderRepo {
    private Map<String, Provider> providerMap;

    public ProviderRepoImpl() {
        this.providerMap = new HashMap<>();
    }

    public Boolean addProvider(Provider provider) {
        if (provider != null) {
            providerMap.put(provider.getProviderId(), provider);
            return true;
        }
        return false;
    }

    @Override
    public Provider getProvider(String providerId) {
        return providerMap.get(providerId);
    }

    @Override
    public Boolean updateState(String providerId, boolean active) {
        Provider provider = getProvider(providerId);
        if (provider != null) {
            provider.setActive(active);
            return true;
        }
        return false;
    }

    @Override
    public Boolean updateProvider(Provider provider) {
        if (provider != null && providerMap.containsKey(provider.getProviderId())) {
            providerMap.put(provider.getProviderId(), provider);
            return true;
        }
        return false;
    }
}
