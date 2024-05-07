import ChatInput from "@/components/chat-input";
import ChatMessages from "@/components/chat-messages";
import Navbar from "@/components/navbar";
import Sidebar from "@/components/sidebar";
import { getChatMessages } from "@/lib/chat";
import { chatsAtom, friendsAtom } from "@/lib/state";
import { useAtomValue } from "jotai";
import { useSession } from "next-auth/react";
import { useRouter } from "next/router";
import { useEffect, useMemo } from "react";

export default function Chat() {
  const router = useRouter();
  const { id } = router.query;
  const { data: session } = useSession();
  const friends = useAtomValue(friendsAtom);
  const recipient = useMemo(
    () => friends.find((f) => f.email === id),
    [id, friends]
  );
  const chatKey = useMemo(
    () =>
      recipient?.email! < session?.user.email!
        ? `${recipient?.email}|${session?.user.email}`
        : `${session?.user.email}|${recipient?.email}`,
    [recipient, session]
  );
  const chats = useAtomValue(chatsAtom);
  const chat = useMemo(() => chats[chatKey], [chatKey, chats]);

  useEffect(() => {
    getChatMessages(session!, recipient!);
  }, [id, session]);

  return (
    <div className="min-h-screen flex">
      <Sidebar />
      <section className="flex flex-col flex-grow">
        <Navbar />
        <div className="flex-grow px-8 py-4">
          <ChatMessages chat={chat} recipient={recipient} />
        </div>
        <div className="px-8 py-4 flex flex-col justify-end">
          <ChatInput recipient={recipient!} />
        </div>
      </section>
    </div>
  );
}
