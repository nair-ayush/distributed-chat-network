export enum MessageType {
  CHAT = "CHAT_MESSAGE",
  LOGIN = "LOGIN",
  LOGOUT = "LOGOUT",
}

export type User = {
  email: string;
  id?: string;
  name?: string;
};

export type Message = {
  type: MessageType;
  sender: User;
};

export type UserMessage = Message & {
  type: MessageType.LOGIN | MessageType.LOGOUT;
  payload?: string;
};

export type ChatMessage = Message & {
  type: MessageType.CHAT;
  payload: string;
  receivers: User[];
};
