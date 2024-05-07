import { ChatMessage, MessageType, User } from "@/types/types";
import { Session } from "next-auth";
import { sendMessage } from "./ws";

export const sendChatMessage = (
  session: Session,
  payload: string,
  receiver: User
) => {
  const message: ChatMessage = {
    type: MessageType.CHAT,
    payload,
    receiver,
    sender: {
      userId: session?.user.email!,
      email: session?.user.email!,
      name: session?.user.name!,
    },
  };
  sendMessage(JSON.stringify(message));
};

export const getChatMessages = async (session: Session, receiver: User) => {
  const message: ChatMessage = {
    type: MessageType.GET_CHAT,
    receiver,
    payload: "",
    sender: {
      userId: session?.user.email!,
      email: session?.user.email!,
      name: session?.user.name!,
    },
  };
  sendMessage(JSON.stringify(message));
};
