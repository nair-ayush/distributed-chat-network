package com.distributed.models;

public class UserMessage extends Message {
  private String payload;
  private static final long serialVersionUID = 4L;

  public UserMessage(User user, MessageType type, String payload) {
    super(user, type);
    this.payload = payload;
  }

  public UserMessage() {
    super();
  }

  public String getPayload() {
    return payload;
  }

  public void setPayload(String payload) {
    this.payload = payload;
  }

  @Override
  public String toString() {
    return "UserMessage [payload=" + payload + ", type=" + this.getType() + "]";
  }
}
