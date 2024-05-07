package com.distributed.models;

public enum MessageType {
  LOGIN,
  LOGOUT,

  CHAT_MESSAGE,
  GET_CHAT_MESSAGES,

  ADD_FRIEND,
  GET_FRIENDS,
  GET_FRIENDS_SUCCESS,

  SERVER_JOINED,
  SERVER_EXITED,
}
