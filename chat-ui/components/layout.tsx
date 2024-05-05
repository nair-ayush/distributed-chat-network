import { handleLogin } from "@/lib/auth";
import { friendsAtom } from "@/lib/state";
import { FriendMessage, Message, MessageType } from "@/types/types";
import { useSetAtom } from "jotai";
import { useSession } from "next-auth/react";
import { PropsWithChildren, useEffect } from "react";
import {
  getWebSocket,
  initializeWebSocket,
  isWebSocketInitialized,
} from "../lib/ws";

const Layout = ({ children }: PropsWithChildren) => {
  const { data: session, status } = useSession();
  const setFriends = useSetAtom(friendsAtom);

  useEffect(() => {
    const handleMessages = (event: any) => {
      const msg: Message = JSON.parse(event.data);
      console.log("Received message:", msg);
      switch (msg.type) {
        case MessageType.GOT_FRIENDS:
          const fMsg = msg as FriendMessage;
          setFriends(fMsg.friends);
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
