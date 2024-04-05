import ChatInput from "@/components/chat-input";
import Navbar from "@/components/navbar";
import { Button } from "@/components/ui/button";
import { Separator } from "@/components/ui/separator";
import { PlusCircleIcon, UserPlusIcon } from "lucide-react";

export default function Dashboard() {
  return (
    <div className="min-h-screen flex">
      <aside className="w-64 p-4 border-r shadow bg-primary-foreground flex flex-col">
        <p className="font-bold">Chats</p>
        <div className="mt-auto flex flex-col">
          <Separator className="my-2" />
          <Button variant="ghost" size="sm" className="text-left justify-start">
            <UserPlusIcon className="mr-2" /> Add a new connection
          </Button>
          <Button variant="ghost" size="sm" className="text-left justify-start">
            <PlusCircleIcon className="mr-2" /> New Chat
          </Button>
        </div>
      </aside>
      <section className="flex flex-col flex-grow">
        <Navbar />
        <div className="flex-grow px-8 py-4">Chat</div>
        <div className="px-8 py-4 flex flex-col justify-end">
          <ChatInput />
        </div>
      </section>
    </div>
  );
}
