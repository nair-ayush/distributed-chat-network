import { TooltipProvider } from "@/components/ui/tooltip";
import "@/styles/globals.css";
import { SessionProvider } from "next-auth/react";
import type { AppProps } from "next/app";

export default function App({
  Component,
  pageProps: { session, ...pageProps },
}: AppProps) {
  return (
    <SessionProvider session={session}>
      <TooltipProvider>
        <Component {...pageProps} />
      </TooltipProvider>
    </SessionProvider>
  );
}
