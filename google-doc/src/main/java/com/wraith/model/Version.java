package org.wraith.model;

public class Version {
    private final String versionId;
    private final String content;
    private final long timestamp;

    public Version(String versionId, String content, long timestamp) {
        this.versionId = versionId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getVersionId() {
        return versionId;
    }

    public String getContent() {
        return content;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
