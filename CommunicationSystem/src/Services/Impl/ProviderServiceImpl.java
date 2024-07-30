package Services.Impl;

import Model.CommunicationChannel;
import Model.Provider;
import Repositories.ProviderRepo;
import Services.ProviderService;
import Services.RequestService;
import Utils.GenerateUUID;

import java.util.Map;

import static Services.Impl.RequestServiceFactory.createRequestService;

public class ProviderServiceImpl implements ProviderService {
    private final ProviderRepo providerRepo;
    private final RequestServiceFactory requestService;

    public ProviderServiceImpl(ProviderRepo providerRepo, RequestServiceFactory requestService) {
        this.providerRepo = providerRepo;
        this.requestService = requestService;
    }

    public String addProvider(String providerName, Map<CommunicationChannel, String> endpoints,
                              Map<String, Map<String, String>> accountCredentials, boolean active) {

        String providerId = GenerateUUID.getUUID();
        Provider provider = new Provider(providerId, providerName, endpoints, accountCredentials, active);

        if (validateProvider(provider)) {
            Boolean status =  providerRepo.addProvider(provider);
            if(status) {
                System.out.println("Provider successfully added: " + providerId);
                return providerId;
            }
        }
        System.out.println("Provider not added");
        return null;
    }

    public Provider getProvider(String providerId) {
        Provider provider =  providerRepo.getProvider(providerId);
        if(provider==null){
            System.out.println("Provider not found with ID: "+ providerId);
            return null;
        }
        System.out.println("Provider found");
        return provider;
    }

    public void updateState(String providerId, boolean active) {
        Provider provider = providerRepo.getProvider(providerId);
        if(provider!=null){
            Boolean currStats = provider.isActive();
            if(currStats == active){
                System.out.println("provider already in same state" + active);
            }
            else{
                providerRepo.updateState(providerId, active);
                System.out.println("provider state updated to "+ active);
            }
        }
        else {
            System.out.println("provider doesn't exists");
        }

    }

    public void updateProvider(Provider provider) {
        if (validateProvider(provider)) {
            providerRepo.updateProvider(provider);
        }
    }

    public void addAccountCredentials(String providerId, String accountType, Map<String, String> credentials) {
        Provider provider = providerRepo.getProvider(providerId);
        if (provider != null) {
            provider.getAccountCredentials().put(accountType, credentials);
            System.out.println("Account credentials added for provider " + providerId + ", account type: " + accountType);
        } else {
            System.out.println("Provider not found with ID: " + providerId);
        }
    }

    public void removeAccountCredentials(String providerId, String accountType) {
        Provider provider = providerRepo.getProvider(providerId);
        if (provider != null) {
            Map<String, Map<String, String>> accountCredentials = provider.getAccountCredentials();
            if (accountCredentials.containsKey(accountType)) {
                accountCredentials.remove(accountType);
                System.out.println("Account credentials removed for provider " + providerId + ", account type: " + accountType);
            } else {
                System.out.println("Account credentials not found for account type: " + accountType);
            }
        } else {
            System.out.println("Provider not found with ID: " + providerId);
        }
    }

    public void addChannel(String providerId, CommunicationChannel channel, String endpoint) {
        Provider provider = providerRepo.getProvider(providerId);
        if (provider != null) {
            provider.getEndpoints().put(channel, endpoint);
            System.out.println("Channel added for provider " + providerId + ", channel: " + channel);
        } else {
            System.out.println("Provider not found with ID: " + providerId);
        }
    }

    public void removeChannel(String providerId, CommunicationChannel channel) {
        Provider provider = providerRepo.getProvider(providerId);
        if (provider != null) {
            Map<CommunicationChannel, String> endpoints = provider.getEndpoints();
            if (endpoints.containsKey(channel)) {
                endpoints.remove(channel);
                System.out.println("Channel removed for provider " + providerId + ", channel: " + channel);
            } else {
                System.out.println("Channel not found for channel: " + channel);
            }
        } else {
            System.out.println("Provider not found with ID: " + providerId);
        }
    }

    private boolean validateProvider(Provider provider) {
        return provider != null && provider.getProviderId() != null && !provider.getProviderId().isEmpty();
    }

    public void processRequest(CommunicationChannel channel, Map<String, Object> args) {
        RequestService requestService = createRequestService(channel, args);

        if (requestService != null) {
            requestService.sendRequest();
            System.out.println("Request processed successfully");
        } else {
            System.out.println("Invalid request service for channel: " + channel);
        }
    }
}
