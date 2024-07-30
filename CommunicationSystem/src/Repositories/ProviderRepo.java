package Repositories;

import Model.Provider;

public interface ProviderRepo {
    Boolean addProvider(Provider provider);
    Provider getProvider(String providerId);
    Boolean updateState(String providerId, boolean active);
    Boolean updateProvider(Provider provider);
}
