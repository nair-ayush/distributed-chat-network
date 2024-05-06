import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { sendChatMessage } from "@/lib/chat";
import { User } from "@/types/types";
import { SendIcon } from "lucide-react";
import { useSession } from "next-auth/react";
import React from "react";

type Props = React.PropsWithChildren<{
  recipient: User;
}>;

export default function ChatInput({ recipient }: Props) {
  const [message, setMessage] = React.useState("");
  const { data: session } = useSession();

  const handleMessageChange: React.ChangeEventHandler<HTMLInputElement> = (
    event
  ) => {
    setMessage(event.target.value);
  };

  const handleMessageKeyDown: React.KeyboardEventHandler<HTMLInputElement> = (
    event
  ) => {
    if (event.key === "Enter") {
      sendChatMessage(session!, message, recipient);
      setMessage("");
    }
  };

  return (
    <div className="flex w-full items-center space-x-2">
      <Input
        type="text"
        placeholder="Please enter a message..."
        value={message}
        onChange={handleMessageChange}
        onKeyDown={handleMessageKeyDown}
      />
      <Button type="submit" variant="secondary" size="icon">
        <SendIcon />
      </Button>
    </div>
  );
}
