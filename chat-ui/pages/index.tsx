import Navbar from "@/components/navbar";
import { Button } from "@/components/ui/button";
import { ExternalLink, GroupIcon } from "lucide-react";
import { useSession } from "next-auth/react";
import { Inter } from "next/font/google";
import Link from "next/link";

const inter = Inter({ subsets: ["latin"] });

export default function Home() {
  const { data: session } = useSession();
  return (
    <main
      className={`flex min-h-screen flex-col items-center ${inter.className}`}
    >
      <Navbar />
      <div className="flex flex-col items-center justify-center py-2">
        <div className="flex flex-col items-center justify-center py-28 w-full px-4 sm:max-w-xl md:max-w-2xl lg:max-w-4xl">
          <h1 className="mb-4 text-4xl font-bold text-center">Chat.io</h1>
          {session && (
            <p className="mb-4 text-xl text-center">
              Welcome,{" "}
              <em className="text-muted-foreground">{session.user?.name}!</em>{" "}
              You are logged in! 🎉
            </p>
          )}
          <p className="mb-4 text-lg text-center">
            A Distributed Chat System for Friends and Family!
          </p>
          <Button asChild variant="outline">
            {session ? (
              <Link href="/dashboard">
                <GroupIcon className="mr-2" />
                Head to the Dashboard
              </Link>
            ) : (
              <Link href="/api/auth/signin">
                <ExternalLink className="mr-2" />
                Login/Signup
              </Link>
            )}
          </Button>
        </div>
      </div>
    </main>
  );
}
