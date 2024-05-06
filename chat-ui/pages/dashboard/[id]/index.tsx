import ChatInput from "@/components/chat-input";
import Navbar from "@/components/navbar";
import NewConnectionBtn from "@/components/new-connection-btn";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { getFriends } from "@/lib/auth";
import { friendsAtom } from "@/lib/state";
import clsx from "clsx";
import { useAtomValue } from "jotai";
import { PlusCircleIcon } from "lucide-react";
import { useSession } from "next-auth/react";
import Link from "next/link";
import { useRouter } from "next/router";
import { useEffect } from "react";

export default function Dashboard() {
  const router = useRouter();
  const { id } = router.query;
  const { data: session } = useSession();
  const friends = useAtomValue(friendsAtom);
  const recipient = friends.find((f) => f.email === id);
  useEffect(() => {
    getFriends(session!);
  }, []);

  return (
    <div className="min-h-screen flex">
      <aside className="w-64 p-4 border-r shadow bg-primary-foreground flex flex-col">
        <p className="font-bold">Chats</p>
        <div className="flex flex-col mt-4">
          {friends?.map((f) => (
            <Button
              asChild
              key={f.userId}
              variant="ghost"
              size="sm"
              className={clsx(
                "text-left justify-start",
                id === f.email && "font-bold"
              )}
            >
              <Link href={`/dashboard/${f.email}`} className="hover:underline">
                {f.name}
              </Link>
            </Button>
          ))}
        </div>
        <div className="mt-auto flex flex-col">
          <Separator className="my-2" />
          <NewConnectionBtn />
          <Button variant="ghost" size="sm" className="text-left justify-start">
            <PlusCircleIcon className="mr-2" /> New Chat
          </Button>
        </div>
      </aside>
      <section className="flex flex-col flex-grow">
        <Navbar />
        <div className="flex-grow px-8 py-4">Chat</div>
        <div className="px-8 py-4 flex flex-col justify-end">
          <ChatInput recipient={recipient!} />
        </div>
      </section>
    </div>
  );
}
