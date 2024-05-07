package com.distributed.models;

import java.util.ArrayList;

public class Chat extends Message {
    private String key;
    private MessageType type;
    private ArrayList<ChatItem> messages;
    private static final long serialVersionUID = 7L;

    public Chat(String key, ArrayList<ChatItem> messages) {
        this.key = key;
        this.messages = messages;
    }

    public Chat(String key) {
        this.key = key;
        messages = new ArrayList<>();
    }

    public ArrayList<ChatItem> getMessages() {
        return messages;
    }

    public String getKey() {
        return key;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public MessageType getType() {
        return type;
    }

    public String toString() {
        return "Chat{" +
                "key='" + key + '\'' +
                ", type=" + type +
                ", messages=" + messages +
                '}';
    }

}
