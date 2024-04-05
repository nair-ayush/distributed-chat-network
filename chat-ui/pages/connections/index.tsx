import Navbar from "@/components/navbar";
import { Inter } from "next/font/google";
const inter = Inter({ subsets: ["latin"] });

const Connections = () => {
  return (
    <main
      className={`flex min-h-screen flex-col items-center ${inter.className}`}
    >
      <Navbar />
      <div className="flex flex-col items-center justify-center py-2">
        <div className="flex flex-col items-center justify-center py-8 w-full px-4 sm:max-w-xl md:max-w-2xl lg:max-w-4xl">
          <h1 className="mb-4 text-4xl font-bold text-center">Friends</h1>
        </div>
      </div>
    </main>
  );
};

export default Connections;
