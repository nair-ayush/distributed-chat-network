package com.distributed.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserMessage extends Message {
  private String payload;

  public UserMessage(User user, MessageType type, String payload) {
    super(user, type);
    this.payload = payload;
  }

  public UserMessage() {
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
