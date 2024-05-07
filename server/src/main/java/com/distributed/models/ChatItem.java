package com.distributed.models;

import java.io.Serializable;

public class ChatItem implements Serializable {
    private String message;
    private String sender;
    private long timestamp;
    private static final long serialVersionUID = 8L;

    public ChatItem(String message, String sender, long timestamp) {
        this.message = message;
        this.sender = sender;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    public long getTimestamp() {
        return timestamp;
    }

}
