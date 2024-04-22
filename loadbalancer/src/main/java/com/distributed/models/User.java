package com.distributed.models;

import java.io.Serializable;
import java.util.Objects;

public class User implements Serializable {
  private String email;
  private String userId;
  private String name;

  public User(String email, String userId, String name) {
    this.email = email;
    this.userId = userId;
    this.name = name;
  }

  public User() {
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getUserId() {
    return userId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  @Override
  public String toString() {
    return "User [email=" + email + ", userId=" + userId + "]";
  }

  @Override
  public boolean equals(Object o) {
    if (o == this)
      return true;
    if (null == o || o.getClass() != this.getClass())
      return false;

    User other = (User) o;

    return Objects.equals(this.userId, other.userId);
  }
}
