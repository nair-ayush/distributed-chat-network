package com.distributed.models;

public enum MessageType {
  LOGIN,
  LOGOUT,

  CHAT_MESSAGE,

  ADD_FRIEND,
  ADD_FRIEND_SUCCESS,
  ADD_FRIEND_FAILURE,

  GET_FRIENDS,
  GET_FRIENDS_SUCCESS,

  SERVER_LEFT,
}
