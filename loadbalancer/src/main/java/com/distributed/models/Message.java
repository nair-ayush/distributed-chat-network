package com.distributed.models;

import java.io.Serializable;

public class Message implements Serializable {
  private User sender;
  private MessageType type;

  public Message(User sender, MessageType type) {
    this.sender = sender;
    this.type = type;
  }

  public Message() {
  }

  public User getSender() {
    return sender;
  }

  public void setSender(User sender) {
    this.sender = sender;
  }

  public MessageType getType() {
    return type;
  }

  public void setType(MessageType type) {
    this.type = type;
  }

  @Override
  public String toString() {
    return "Message [sender=" + sender + ", type=" + type + "]";
  }

}
