package Services.Impl;

import Services.RequestService;

import java.util.Map;

public class EmailRequestService implements RequestService {

    private final String sender;
    private final String receiver;
    private final String subject;
    private final String message;

    public EmailRequestService(Map<String, Object> args) {
        this.sender = (String) args.get("sender");
        this.receiver = (String) args.get("receiver");
        this.subject = (String) args.get("subject");
        this.message = (String) args.get("message");
        validateRequiredParams();
    }

    private void validateRequiredParams() {
        if (sender == null || receiver == null || subject == null || message == null) {
            throw new IllegalArgumentException("Missing required parameters for Email request");
        }
    }

    @Override
    public void sendRequest() {
        System.out.println("Sending email: Sender-" + sender + ", Receiver-" + receiver +
                ", Subject-" + subject + ", Message-" + message);
    }
}
