package Model;

import java.util.Map;

public class Provider {

    private String providerId;
    private String providerName;
    private Map<CommunicationChannel, String> endpoints;
    private Map<String, Map<String, String>> accountCredentials;

    public Map<CommunicationChannel, String> getEndpoints() {
        return endpoints;
    }

    public void setEndpoints(Map<CommunicationChannel, String> endpoints) {
        this.endpoints = endpoints;
    }

    public Map<String, Map<String, String>> getAccountCredentials() {
        return accountCredentials;
    }

    public void setAccountCredentials(Map<String, Map<String, String>> accountCredentials) {
        this.accountCredentials = accountCredentials;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    private boolean active;

    public Provider(String providerId, String providerName, Map<CommunicationChannel, String> endpoints,
                    Map<String, Map<String, String>> accountCredentials, boolean active) {
        this.providerId = providerId;
        this.providerName = providerName;
        this.endpoints = endpoints;
        this.accountCredentials = accountCredentials;
        this.active = active;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public Boolean isCommunicationTypeSupported(CommunicationChannel channel) {
        return active && endpoints.containsKey(channel);
    }

    public void sendRequest(CommunicationChannel channel, Map<String, String> requestData) {
        if (isCommunicationTypeSupported(channel)) {
            System.out.println("Sending " + channel + " request through provider " + providerId);
        } else {
            System.out.println("Provider " + providerId + " is inactive or doesn't support the channel");
        }
    }
}
