import { getFriends } from "@/lib/auth";
import { friendsAtom } from "@/lib/state";
import clsx from "clsx";
import { useAtomValue } from "jotai";
import { useSession } from "next-auth/react";
import Link from "next/link";
import { useRouter } from "next/router";
import { useEffect } from "react";
import NewConnectionBtn from "./new-connection-btn";
import { Button } from "./ui/button";
import { Separator } from "./ui/separator";

const Sidebar = () => {
  const router = useRouter();
  const { id } = router.query;
  const { data: session } = useSession();
  const friends = useAtomValue(friendsAtom);
  useEffect(() => {
    getFriends(session!);
  }, [session, getFriends]);

  return (
    <aside className="w-64 p-4 border-r shadow bg-primary-foreground flex flex-col">
      <p className="font-bold text-2xl mt-5">Chats</p>
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
            <Link href={`/chat/${f.email}`} className="hover:underline">
              {f.name}
            </Link>
          </Button>
        ))}
      </div>
      <div className="mt-auto flex flex-col">
        <Separator className="my-2" />
        <NewConnectionBtn />
      </div>
    </aside>
  );
};

export default Sidebar;
