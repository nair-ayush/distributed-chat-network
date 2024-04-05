import { Button } from "@/components/ui/button";
import { Input } from "@/components/ui/input";
import { SendIcon } from "lucide-react";

export default function ChatInput() {
  return (
    <div className="flex w-full items-center space-x-2">
      <Input type="text" placeholder="Please enter a message..." />
      <Button type="submit" variant="secondary" size="icon">
        <SendIcon />
      </Button>
    </div>
  );
}
