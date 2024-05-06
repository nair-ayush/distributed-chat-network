package com.distributed.models;

public class ChatMessage extends Message {
  private String payload;
  private User receiver;
  private static final long serialVersionUID = 3L;

  public ChatMessage(User sender, MessageType type, String payload, User receiver) {
    super(sender, type);
    this.payload = payload;
    this.receiver = receiver;
  }

  public ChatMessage() {
    super();
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String message) {
    this.payload = message;
  }

  public User getReceiver() {
    return receiver;
  }

  public void setReceiver(User receiver) {
    this.receiver = receiver;
  }

  @Override
  public String toString() {
    return "ChatMessage [payload=" + payload + ", receiver=" + receiver + "]";
  }

}
