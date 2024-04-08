import { getWebSocketInstance } from "@/lib/ws";
import { MessageType, UserMessage } from "@/types/types";
import { ExternalLink } from "lucide-react";
import { signOut, useSession } from "next-auth/react";
import Link from "next/link";
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar";
import { Button } from "./ui/button";
import { Tooltip, TooltipContent, TooltipTrigger } from "./ui/tooltip";

const Navbar = () => {
  const { data: session } = useSession();

  const handleLogout = () => {
    signOut();
    const ws = getWebSocketInstance();
    const message: UserMessage = {
      type: MessageType.LOGOUT,
      sender: {
        id: session?.user?.id!,
        email: session?.user?.email!,
        name: session?.user?.name!,
      },
    };
    try {
      ws.send(JSON.stringify(message));
    } catch (error) {
      console.error("Error sending logout message: ", error);
    }
  };
  return (
    <nav className="py-4 px-4 sm:px-8 lg:px-16 w-full">
      <div className="flex justify-between items-center">
        <Link href="/" className="text-base sm:text-2xl font-bold">
          Chat.io
        </Link>
        <div className="flex ml-auto text-sm sm:text-base gap-4">
          {session ? (
            <>
              <Button asChild variant="ghost">
                <Link href="/dashboard" className="hover:underline">
                  Dashboard
                </Link>
              </Button>
              <Button asChild variant="ghost">
                <Link href="/connections" className="hover:underline">
                  Connections
                </Link>
              </Button>
              <Tooltip>
                <TooltipTrigger>
                  <Button asChild variant="outline" onClick={handleLogout}>
                    <Link href="/api/auth/signout">
                      <Avatar className="mr-2 h-8 w-8">
                        <AvatarImage
                          src={session.user?.image!}
                          alt={session.user?.name!}
                        />
                        <AvatarFallback>
                          {session.user?.name
                            ?.split(" ")
                            .map((w) => w[0])
                            .join("")}
                        </AvatarFallback>
                      </Avatar>
                      {session.user?.name}
                    </Link>
                  </Button>
                </TooltipTrigger>
                <TooltipContent>Logout</TooltipContent>
              </Tooltip>
            </>
          ) : (
            <Button asChild variant="outline">
              <Link href="/api/auth/signin">
                <ExternalLink className="mr-2" />
                Login/Signup
              </Link>
            </Button>
          )}
        </div>
      </div>
    </nav>
  );
};

export default Navbar;
