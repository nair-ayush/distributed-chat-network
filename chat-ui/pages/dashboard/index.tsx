import Navbar from "@/components/navbar";
import { Button } from "@/components/ui/button";
import { Card, CardFooter, CardHeader, CardTitle } from "@/components/ui/card";
import { getFriends } from "@/lib/auth";
import { friendsAtom } from "@/lib/state";
import { useAtomValue } from "jotai";
import { useSession } from "next-auth/react";
import { Inter } from "next/font/google";
import Link from "next/link";
import { useEffect } from "react";
const inter = Inter({ subsets: ["latin"] });

const Dashboard = () => {
  const { data: session } = useSession();
  const friends = useAtomValue(friendsAtom);

  useEffect(() => {
    getFriends(session!);
  }, [session, getFriends]);

  return (
    <main
      className={`flex min-h-screen flex-col items-center ${inter.className}`}
    >
      <Navbar />
      <div className="flex flex-col py-2">
        <div className="flex flex-col items-center justify-center py-8 w-full px-4 sm:max-w-xl md:max-w-2xl lg:max-w-4xl">
          <h1 className="mb-4 text-4xl font-bold text-center">Friends</h1>
          <div className="grid grid-cols-2 lg:grid-cols-3 gap-4">
            {friends.length ? (
              friends.map((friend) => (
                <Card key={friend.email} className="w-[250px]">
                  <CardHeader>
                    <CardTitle className="text-xl">{friend.name}</CardTitle>
                  </CardHeader>
                  <CardFooter className="flex justify-end">
                    <Button size="sm" variant="default">
                      <Link href={`/chat/${friend.email}`}>Chat</Link>
                    </Button>
                  </CardFooter>
                </Card>
              ))
            ) : (
              <p className="text-center text-gray-500">No friends yet</p>
            )}
          </div>
        </div>
      </div>
    </main>
  );
};

export default Dashboard;
