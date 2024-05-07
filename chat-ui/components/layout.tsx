import { handleLogin } from "@/lib/auth";
import { chatsAtom, friendsAtom } from "@/lib/state";
import {
  Chat,
  ChatMessage,
  FriendMessage,
  Message,
  MessageType,
} from "@/types/types";
import { useSetAtom } from "jotai";
import { useSession } from "next-auth/react";
import { PropsWithChildren, useCallback, useEffect } from "react";
import {
  getWebSocket,
  initializeWebSocket,
  isWebSocketInitialized,
} from "../lib/ws";
import { useToast } from "./ui/use-toast";

const Layout = ({ children }: PropsWithChildren) => {
  const { data: session, status } = useSession();
  const { toast } = useToast();
  const setFriends = useSetAtom(friendsAtom);
  const setChats = useSetAtom(chatsAtom);

  const handleMessages = useCallback((event: any) => {
    const msg: Message = JSON.parse(event.data);
    console.log("Received message:", msg);
    switch (msg.type) {
      case MessageType.GET_FRIENDS_SUCCESS:
      case MessageType.ADD_FRIEND_SUCCESS:
        const fMsg = msg as FriendMessage;
        setFriends(fMsg.friends);
        break;

      case MessageType.ADD_FRIEND_FAILURE:
        const fMsg2 = msg as FriendMessage;
        toast({
          variant: "destructive",
          title: "Uh oh! Something went wrong.",
          description: fMsg2.desc,
        });
        break;

      case MessageType.CHAT:
        const cMsg = msg as ChatMessage;
        const key =
          cMsg.receiver.email < cMsg.sender.email
            ? `${cMsg.receiver.email}|${cMsg.sender.email}`
            : `${cMsg.sender.email}|${cMsg.receiver.email}`;
        setChats((prev) => ({
          ...prev,
          [key]: {
            ...prev[key],
            messages: [
              ...(prev[key]?.messages || []),
              {
                message: cMsg.payload,
                sender: cMsg.sender.email,
                timestamp: 0,
              },
            ],
          },
        }));
        break;

      case MessageType.GET_CHAT_SUCCESS:
        const cMsg3 = msg as Chat;
        setChats((prev) => ({ ...prev, [cMsg3.key!]: cMsg3 }));
        break;

      default:
        break;
    }
  }, []);

  useEffect(() => {
    const initWebSocketAndSendLoginMessage = async () => {
      try {
        if (
          session &&
          status === "authenticated" &&
          !isWebSocketInitialized()
        ) {
          await initializeWebSocket();
          getWebSocket().addEventListener("message", handleMessages);
          handleLogin(session!);
        }
      } catch (error) {
        console.error("Failed to initialize WebSocket:", error);
      }
    };

    initWebSocketAndSendLoginMessage();

    return () => {
      if (isWebSocketInitialized()) {
        getWebSocket().removeEventListener("message", handleMessages);
      }
    };
  }, [session, status]);

  return <div>{children}</div>;
};

export default Layout;
