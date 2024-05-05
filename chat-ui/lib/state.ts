import { User } from "@/types/types";
import { atom } from "jotai";

export const friendsAtom = atom<User[]>([]);
