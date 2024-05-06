import { MessageType, UserMessage } from "@/types/types";
import { Session } from "next-auth";
import { signOut } from "next-auth/react";
import { closeWebSocket, sendMessage } from "./ws";

export const handleLogin = (session: Session) => {
  const message: UserMessage = {
    type: MessageType.LOGIN,
    payload: session?.user.email || "",
    sender: {
      userId: session?.user.userId!,
      email: session?.user.email!,
      name: session?.user.name!,
    },
  };
  sendMessage(JSON.stringify(message));
};

export const handleLogout = (session: Session) => {
  const message: UserMessage = {
    type: MessageType.LOGOUT,
    payload: session?.user.email || "",
    sender: {
      userId: session?.user?.userId!,
      email: session?.user?.email!,
      name: session?.user?.name!,
    },
  };
  sendMessage(JSON.stringify(message));
  closeWebSocket();
  signOut();
};

export const addFriend = (session: Session, email: string) => {
  const message: UserMessage = {
    type: MessageType.ADD_FRIEND,
    payload: email,
    sender: {
      userId: session?.user.userId!,
      email: session?.user.email!,
      name: session?.user.name!,
    },
  };
  sendMessage(JSON.stringify(message));
};

export const getFriends = (session: Session) => {
  const message: UserMessage = {
    type: MessageType.GET_FRIENDS,
    payload: "",
    sender: {
      userId: session?.user.userId!,
      email: session?.user.email!,
      name: session?.user.name!,
    },
  };
  console.log(JSON.stringify(message, null, 2));
  sendMessage(JSON.stringify(message));
};
