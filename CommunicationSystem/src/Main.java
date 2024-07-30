import Model.CommunicationChannel;
import Model.Provider;
import Repositories.Impl.ProviderRepoImpl;
import Repositories.ProviderRepo;
import Services.Impl.ProviderServiceImpl;
import Services.Impl.RequestServiceFactory;
import Services.RequestService;

import java.util.HashMap;
import java.util.Map;


public class Main {
    public static void main(String[] args) {
        //1. creating objects
        ProviderRepo providerRepo = new ProviderRepoImpl();
        RequestServiceFactory requestService = new RequestServiceFactory();
        ProviderServiceImpl providerService = new ProviderServiceImpl(providerRepo, requestService);

        //2. preparing data
        Map<CommunicationChannel, String> endpoints1 = new HashMap<>();
        endpoints1.put(CommunicationChannel.EMAIL, "https://email-provider1-api.com");
        endpoints1.put(CommunicationChannel.SMS, "https://sms-provider1-api.com");

        Map<CommunicationChannel, String> endpoints2 = new HashMap<>();
        endpoints1.put(CommunicationChannel.EMAIL, "https://email-provider1-api.com");
        endpoints1.put(CommunicationChannel.SMS, "https://sms-provider1-api.com");

        Map<String, Map<String, String>> accountCredentials1 = new HashMap<>();
        Map<String, Map<String, String>> accountCredentials2 = new HashMap<>();

        Map<String, String> highLatencyCredentials = new HashMap<>();
        highLatencyCredentials.put("username", "high_latency_user");
        highLatencyCredentials.put("password", "high_latency_password");
        
        Map<String, String> highUserbaseCredentials = new HashMap<>();
        highUserbaseCredentials.put("username", "high_userbase_user");
        highUserbaseCredentials.put("password", "high_userbase_password");
        
        accountCredentials1.put("high_latency", highLatencyCredentials);
        accountCredentials1.put("high_userbase", highUserbaseCredentials);
        accountCredentials2.put("high_userbase", highUserbaseCredentials);

        //3. Creating providers
        String providerId1 = providerService.addProvider("Provider1", endpoints1, accountCredentials1, true);
        String providerId2 = providerService.addProvider("Provider2", endpoints2, accountCredentials2, true);

        //4. Getting providers
        Provider provider1 = providerService.getProvider(providerId1);
        Provider provider2 = providerService.getProvider("123");

        //5. Updating state of providers
        providerService.updateState(providerId1, false);

        //6. Adding more channels
        providerService.addChannel(providerId1, CommunicationChannel.SOUNDBOX, "https://soundbox-provider1-api.com");


        //7. sending request for SMS type
        Map<String, Object> smsArgs = new HashMap<>();
        smsArgs.put("mobileNumber", "1234567890");
        smsArgs.put("message", "SMS Message");

        providerService.processRequest(CommunicationChannel.SMS, smsArgs);
    }
}