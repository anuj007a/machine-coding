package Services.Impl;

import Services.RequestService;

import java.util.Map;

public class SmsRequestService implements RequestService {

    private final String mobileNumber;
    private final String message;

    public SmsRequestService(Map<String, Object> args) {
        this.mobileNumber = (String) args.get("mobileNumber");
        this.message = (String) args.get("message");
        validateRequiredParams();
    }

    private void validateRequiredParams() {
        if (mobileNumber == null || message == null) {
            throw new IllegalArgumentException("Missing required parameters for SMS request");
        }
    }

    @Override
    public void sendRequest() {
        System.out.println("Sending SMS: Mobile Number-" + mobileNumber + ", Message-" + message);
    }
}
