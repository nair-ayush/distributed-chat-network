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
  console.log(JSON.stringify(message, null, 2));
  sendMessage(JSON.stringify(message));
};
