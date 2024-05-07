import { Chat, User } from "@/types/types";
import { Label } from "./ui/label";

type Props = {
  chat: Chat | null;
  recipient: User | null | undefined;
};

export default function ChatMessage({ chat, recipient }: Props) {
  return (
    <div className="flex flex-col space-y-2 h-full justify-end">
      {chat && chat?.messages && chat.messages.length ? (
        chat?.messages.map((msg, index) => (
          <div
            key={index}
            className={`flex space-x-2 ${
              msg.sender === recipient?.email ? "justify-start" : "justify-end"
            }`}
          >
            <div
              className={`px-4 py-2 rounded-lg ${
                msg.sender === recipient?.email
                  ? "bg-gray-200 text-gray-900"
                  : "bg-blue-500 text-white"
              }`}
            >
              {msg.message}
            </div>
          </div>
        ))
      ) : (
        <Label className="text-center italic text-gray-700">
          No messages yet
        </Label>
      )}
    </div>
  );
}
