import { TooltipProvider } from "@/components/ui/tooltip";
import { closeWebSocket, initializeWebSocket } from "@/lib/ws";
import "@/styles/globals.css";
import { SessionProvider } from "next-auth/react";
import type { AppProps } from "next/app";
import { useEffect } from "react";

export default function App({
  Component,
  pageProps: { session, ...pageProps },
}: AppProps) {
  useEffect(() => {
    initializeWebSocket();

    return () => {
      closeWebSocket();
    };
  }, []);
  return (
    <SessionProvider session={session}>
      <TooltipProvider>
        <Component {...pageProps} />
      </TooltipProvider>
    </SessionProvider>
  );
}
