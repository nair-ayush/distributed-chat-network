"use client";
import {
  Dialog,
  DialogClose,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { addFriend } from "@/lib/auth";
import { UserPlusIcon } from "lucide-react";
import { useSession } from "next-auth/react";
import { useState } from "react";
import { Button } from "./ui/button";

const NewConnectionBtn = () => {
  const { data: session } = useSession();
  const [newEmail, setNewEmail] = useState<string>("");

  return (
    <Dialog>
      <DialogTrigger asChild>
        <Button variant="ghost" size="sm" className="text-left justify-start">
          <UserPlusIcon className="mr-2" /> Add a new connection
        </Button>
      </DialogTrigger>
      <DialogContent className="sm:max-w-[425px]">
        <DialogHeader>
          <DialogTitle>Add new connection...</DialogTitle>
          <DialogDescription>
            ...by entering their username/email
          </DialogDescription>
        </DialogHeader>
        <div className="grid gap-4 py-4">
          <div className="grid grid-cols-4 items-center gap-4">
            <Label htmlFor="username" className="text-right">
              Email
            </Label>
            <Input
              id="username"
              type="email"
              className="col-span-3"
              value={newEmail}
              onChange={(e) => setNewEmail(e.target.value)}
            />
          </div>
        </div>
        <DialogFooter>
          <DialogClose asChild>
            <Button
              variant="default"
              type="submit"
              onClick={() => {
                addFriend(session!, newEmail);
                setNewEmail("");
              }}
            >
              Done
            </Button>
          </DialogClose>
        </DialogFooter>
      </DialogContent>
    </Dialog>
  );
};

export default NewConnectionBtn;
