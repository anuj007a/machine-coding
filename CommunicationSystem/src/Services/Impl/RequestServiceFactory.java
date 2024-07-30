package Services.Impl;

import Model.CommunicationChannel;
import Services.RequestService;

import java.util.Map;

public class RequestServiceFactory {

    public static RequestService createRequestService(CommunicationChannel channel, Map<String, Object> args) {
        switch (channel) {
            case EMAIL:
                validateArgs(args, "sender", "receiver", "subject", "message");
                return new EmailRequestService(args);
            case SMS:
                validateArgs(args, "mobileNumber", "message");
                return new SmsRequestService(args);
            case SOUNDBOX:
                validateArgs(args, "soundId", "volume");
                return new SoundboxRequestService(args);
            default:
                throw new IllegalArgumentException("Invalid Communication Channel");
        }
    }

    private static void validateArgs(Map<String, Object> args, String... requiredKeys) {
        for (String key : requiredKeys) {
            if (!args.containsKey(key)) {
                throw new IllegalArgumentException("Missing required argument: " + key);
            }
        }
    }
}
