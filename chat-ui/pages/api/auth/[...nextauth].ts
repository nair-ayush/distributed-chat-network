import { getWebSocketInstance } from "@/lib/ws";
import { MessageType, UserMessage } from "@/types/types";
import NextAuth, { AuthOptions } from "next-auth";
import GithubProvider from "next-auth/providers/github";

export const authOptions: AuthOptions = {
  // Configure one or more authentication providers
  providers: [
    GithubProvider({
      clientId: process.env.GITHUB_CLIENT_ID!,
      clientSecret: process.env.GITHUB_CLIENT_SECRET!,
    }),
    // ...add more providers here
  ],
  callbacks: {
    async signIn({ user, account, profile, email, credentials }) {
      const message: UserMessage = {
        type: MessageType.LOGIN,
        payload: user.email || "",
        sender: {
          id: user.id!,
          email: user.email!,
          name: user.name!,
        },
      };
      console.log({ user, account, profile, email, credentials });
      try {
        getWebSocketInstance().send(JSON.stringify(message));
      } catch (error) {
        console.error("Error sending login message: ", error);
      }

      return true;
    },
    async session({ session, user, token }) {
      console.log({ session, user, token });
      return { ...session, user: { ...session.user, id: token.sub } };
    },
  },
};

export default NextAuth(authOptions);
