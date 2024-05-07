export enum MessageType {
  GET_CHAT = "GET_CHAT_MESSAGES",
  GET_CHAT_SUCCESS = "GET_CHAT_MESSAGES_SUCCESS",
  CHAT = "CHAT_MESSAGE",
  LOGIN = "LOGIN",
  LOGOUT = "LOGOUT",
  ADD_FRIEND = "ADD_FRIEND",
  ADD_FRIEND_SUCCESS = "ADD_FRIEND_SUCCESS",
  ADD_FRIEND_FAILURE = "ADD_FRIEND_FAILURE",
  GET_FRIENDS = "GET_FRIENDS",
  GET_FRIENDS_SUCCESS = "GET_FRIENDS_SUCCESS",
}

export type User = {
  email: string;
  userId?: string;
  name?: string;
};

export type ChatItem = {
  sender: User;
  message: string;
  timestamp: number;
};

export type Chat = Message & {
  type: MessageType.GET_CHAT_SUCCESS;
  messages?: ChatItem[];
  key?: string;
};

export type Message = {
  type: MessageType;
  sender: User;
};

export type FriendMessage = Message & {
  type:
    | MessageType.GET_FRIENDS_SUCCESS
    | MessageType.ADD_FRIEND_SUCCESS
    | MessageType.ADD_FRIEND_FAILURE;
  friends: User[];
  desc?: string;
};

export type UserMessage = Message & {
  type:
    | MessageType.LOGIN
    | MessageType.LOGOUT
    | MessageType.ADD_FRIEND
    | MessageType.GET_FRIENDS;
  payload?: string;
};

export type ChatMessage = Message & {
  type: MessageType.CHAT | MessageType.GET_CHAT;
  payload: string;
  receiver: User;
};
