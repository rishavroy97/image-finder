import type { Metadata } from "next";
import { Inter } from "next/font/google";
import "./globals.css";
import Footer from "./ui/Footer";
import Header from "./ui/Header";

const inter = Inter({ subsets: ["latin"] });

export const metadata: Metadata = {
  title: "ImageFinder",
  description: "Retrieve images from Website",
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html lang="en">
      <body
        className={`${inter.className} flex flex-col justify-center items-center bg-slate-200 dark:bg-slate-900  text-slate-900 dark:text-slate-200`}
      >
        <Header />
        <div className="flex min-h-[70vh]">{children}</div>
        <Footer />
      </body>
    </html>
  );
}
