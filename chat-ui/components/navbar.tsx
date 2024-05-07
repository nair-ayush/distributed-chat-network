import { handleLogout } from "@/lib/auth";
import { ExternalLink } from "lucide-react";
import { useSession } from "next-auth/react";
import Link from "next/link";
import { Avatar, AvatarFallback, AvatarImage } from "./ui/avatar";
import { Button } from "./ui/button";
import { Tooltip, TooltipContent, TooltipTrigger } from "./ui/tooltip";

const Navbar = () => {
  const { data: session } = useSession();

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
              <Tooltip>
                <TooltipTrigger>
                  <Button
                    asChild
                    variant="outline"
                    onClick={() => handleLogout(session)}
                  >
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
