package Services;

import Model.CommunicationChannel;
import Model.Provider;

import java.util.Map;

public interface ProviderService {
    String addProvider(String providerName, Map<CommunicationChannel, String> endpoints,
                       Map<String, Map<String, String>> accountCredentials, boolean active);

    Provider getProvider(String providerId);

    void updateState(String providerId, boolean active);

    void updateProvider(Provider provider);

    void addAccountCredentials(String providerId, String accountType, Map<String, String> credentials);

    void removeAccountCredentials(String providerId, String accountType);

    void addChannel(String providerId, CommunicationChannel channel, String endpoint);

    void removeChannel(String providerId, CommunicationChannel channel);

    void processRequest(CommunicationChannel channel, Map<String, Object> args);
}
