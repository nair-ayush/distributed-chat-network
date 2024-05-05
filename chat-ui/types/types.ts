export enum MessageType {
  CHAT = "CHAT_MESSAGE",
  LOGIN = "LOGIN",
  LOGOUT = "LOGOUT",
  FRIEND_REQUEST = "FRIEND_MESSAGE",
  GET_FRIENDS = "GET_FRIENDS",
  GOT_FRIENDS = "GOT_FRIENDS",
}

export type User = {
  email: string;
  userId?: string;
  name?: string;
};

export type Message = {
  type: MessageType;
  sender: User;
};

export type FriendMessage = Message & {
  type: MessageType.GOT_FRIENDS;
  friends: User[];
};

export type UserMessage = Message & {
  type:
    | MessageType.LOGIN
    | MessageType.LOGOUT
    | MessageType.FRIEND_REQUEST
    | MessageType.GET_FRIENDS;
  payload?: string;
};

export type ChatMessage = Message & {
  type: MessageType.CHAT;
  payload: string;
  receivers: User[];
};
