package com.distributed.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatMessage extends Message {
  private String message;
  private User[] receivers;

  public ChatMessage(User sender, String message, User[] receivers) {
    super(sender, MessageType.CHAT_MESSAGE);
    this.message = message;
    this.receivers = receivers;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public User[] getReceivers() {
    return receivers;
  }

  public void setReceivers(User[] receivers) {
    this.receivers = receivers;
  }

  @Override
  public String toString() {
    return "ChatMessage [message=" + message + ", receivers=" + receivers + "]";
  }

}
