import { handleLogin } from "@/lib/auth";
import { useSession } from "next-auth/react";
import { PropsWithChildren, useEffect } from "react";
import { initializeWebSocket, isWebSocketInitialized } from "../lib/ws";

const Layout = ({ children }: PropsWithChildren) => {
  const { data: session, status } = useSession();

  useEffect(() => {
    const initWebSocketAndSendLoginMessage = async () => {
      try {
        if (
          session &&
          status === "authenticated" &&
          !isWebSocketInitialized()
        ) {
          await initializeWebSocket();
          handleLogin(session!);
        }
      } catch (error) {
        console.error("Failed to initialize WebSocket:", error);
      }
    };

    initWebSocketAndSendLoginMessage();
  }, [session, status]);

  return <div>{children}</div>;
};

export default Layout;
