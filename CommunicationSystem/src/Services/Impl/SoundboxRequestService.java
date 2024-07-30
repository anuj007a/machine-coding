package Services.Impl;

import Services.RequestService;

import java.util.Map;

public class SoundboxRequestService implements RequestService {

    private final String soundId;
    private final int volume;

    public SoundboxRequestService(Map<String, Object> args) {
        this.soundId = (String) args.get("soundId");
        this.volume = (int) args.get("volume");
        validateRequiredParams();
    }

    private void validateRequiredParams() {
        if (soundId == null) {
            throw new IllegalArgumentException("Missing required parameter for Soundbox request: soundId");
        }
        if (volume < 0 || volume > 100) {
            throw new IllegalArgumentException("Invalid volume level for Soundbox request: " + volume);
        }
    }

    @Override
    public void sendRequest() {
        System.out.println("Playing sound: Sound ID-" + soundId + ", Volume-" + volume);
    }
}
