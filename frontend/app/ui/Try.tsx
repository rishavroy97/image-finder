import Link from "next/link";

const Try = () => {
  
  return (
      <Link href={"/search"}>
        <button className="btn btn-success text-lg font-medium">
          {`Try it out ->`}
        </button>
      </Link>
  );
};

export default Try;
