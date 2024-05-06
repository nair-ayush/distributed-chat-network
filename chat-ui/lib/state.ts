import { Chat, User } from "@/types/types";
import { atom } from "jotai";

export const friendsAtom = atom<User[]>([]);
export const chatsAtom = atom<Record<string, Chat>>({});
