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
    async session({ session, token }) {
      return { ...session, user: { ...session.user, id: token.sub } };
    },
  },
};

export default NextAuth(authOptions);
