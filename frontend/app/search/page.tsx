"use client";

import { searchImages, State } from "@/app/lib/actions";
import { SearchForm } from "@/app/ui/SearchForm";
import Image from "next/image";
import { useActionState } from "react";

const Search = () => {
  const initialState: State = { message: null, errors: {} };
  const [state, formAction, isPending] = useActionState(
    searchImages,
    initialState
  );

  const reset = () => {
    formAction(new FormData());
  };

  return (
    <>
      {!state.data && (
        <SearchForm
          state={state}
          formAction={formAction}
          isPending={isPending}
        />
      )}
      {state.data && (
        <div className="flex flex-col justify-center items-center">
          <button className="btn btn-warning text-lg mb-5" onClick={reset}>
            Search Again
          </button>
          <div>{state.data?.name}</div>
          <div>{state.data?.url}</div>
          <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
            {state.data?.images.map((img: any) => {
              return (
                <div className="card" key={img.id}>
                  <div className="card-body">
                    <Image
                      alt={img.id}
                      src={img.url}
                      width={200}
                      height={200}
                    />
                  </div>
                </div>
              );
            })}
          </div>
        </div>
      )}
    </>
  );
};

export default Search;
