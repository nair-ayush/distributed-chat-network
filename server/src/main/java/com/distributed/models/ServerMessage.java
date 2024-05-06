package com.distributed.models;

public class ServerMessage extends Message {
  private String message;
  private static final long serialVersionUID = 5L;

  public ServerMessage(String message, MessageType type) {
    super(new User(), type);
    this.message = message;
  }

  public ServerMessage() {
    super();
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String toString() {
    return "ServerMessage [message=" + message + "]";
  }

}
