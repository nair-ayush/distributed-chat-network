import { handleLogin } from "@/lib/auth";
import { friendsAtom } from "@/lib/state";
import {
  Chat,
  ChatMessage,
  FriendMessage,
  Message,
  MessageType,
} from "@/types/types";
import { useSetAtom } from "jotai";
import { useSession } from "next-auth/react";
import { PropsWithChildren, useEffect } from "react";
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

  useEffect(() => {
    const handleMessages = (event: any) => {
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
          console.log("Received chat message:", cMsg);
          break;

        case MessageType.GET_CHAT_SUCCESS:
          const cMsg3 = msg as Chat;
          console.log("Received chat:", cMsg3);
          break;

        default:
          break;
      }
    };

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

  // useEffect(() => {
  // }, []);

  return <div>{children}</div>;
};

export default Layout;
