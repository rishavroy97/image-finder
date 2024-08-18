import Link from "next/link";

const Header = () => {
  return (
    <div className="flex flex-row p-2 lg:p-4 justify-center items-center min-h-[15vh]">
      <Link href={"/"}>
        <h1 className="text-5xl font-bold">ImageFinder</h1>
      </Link>
    </div>
  );
};

export default Header;
