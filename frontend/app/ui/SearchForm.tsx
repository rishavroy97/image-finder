import { State } from "@/app/lib/actions";

export const SearchForm = ({
  state,
  formAction,
  isPending,
}: {
  state: State;
  formAction: (payload: FormData) => void;
  isPending: boolean;
}) => {
  return (
    <form action={formAction}>
      <div className="rounded-md bg-gray-50 p-4 md:p-6">
        {/* URL */}
        <div className="mb-4">
          <label htmlFor="url" className="mb-2 block text-sm font-medium">
            URL
          </label>
          <input
            type="url"
            id="url"
            name="url"
            className="bg-slate-50 border border-slate-300 text-slate-900 text-sm rounded-lg block w-full p-2.5 dark:bg-slate-700 dark:border-slate-600 dark:placeholder-slate-400 dark:text-slate-100"
            placeholder="https://pytorch.org/"
            required
          />
          <div id="url-error" aria-live="polite" aria-atomic="true">
            {state.errors?.url &&
              state.errors.url.map((error: string) => (
                <p className="mt-2 text-sm text-red-500" key={error}>
                  {error}
                </p>
              ))}
          </div>
        </div>

        {/* Name */}
        <div className="mb-4">
          <label htmlFor="name" className="mb-2 block text-sm font-medium">
            Name
          </label>
          <div className="relative mt-2 rounded-md">
            <div className="relative">
              <input
                type="text"
                id="name"
                name="name"
                className="bg-slate-50 border border-slate-300 text-slate-900 text-sm rounded-lg block w-full p-2.5 dark:bg-slate-700 dark:border-slate-600 dark:placeholder-slate-400 dark:text-slate-100"
                placeholder="PyTorch"
                required
              />
            </div>
          </div>
          <div id="name-error" aria-live="polite" aria-atomic="true">
            {state.errors?.name &&
              state.errors.name.map((error: string) => (
                <p className="mt-2 text-sm text-red-500" key={error}>
                  {error}
                </p>
              ))}
          </div>
        </div>

        {/* Depth */}
        <div className="mb-4">
          <label htmlFor="depth" className="mb-2 block text-sm font-medium">
            Depth
          </label>
          <div className="relative mt-2 rounded-md">
            <div className="relative">
              <input
                type="number"
                id="depth"
                min="1"
                max="20"
                name="depth"
                placeholder="2"
                className="bg-gray-50 border border-slate-300 text-slate-900 text-sm rounded-lg block w-full p-2.5 dark:bg-slate-700 dark:border-slate-600 dark:placeholder-slate-400 dark:text-slate-100"
                required
              />
            </div>
          </div>
          <div id="depth-error" aria-live="polite" aria-atomic="true">
            {state.errors?.depth &&
              state.errors.depth.map((error: string) => (
                <p className="mt-2 text-sm text-red-500" key={error}>
                  {error}
                </p>
              ))}
          </div>
        </div>
        <div id="form-message" aria-live="polite" aria-atomic="true">
          {state.message && (
            <p className="mt-2 text-sm text-red-500" key={state.message}>
              {state.message}
            </p>
          )}
        </div>
      </div>
      <div className="mt-6 flex justify-end gap-4">
        <button
          type="submit"
          className="btn btn-success text-md disabled:bg-success disabled:opacity-70 disabled:text-primary-content"
          disabled={isPending}
        >
          {isPending ? <span>Fetching Images</span> : <span>Submit</span>}
          {isPending && (
            <span className="loading loading-spinner loading-md"></span>
          )}
        </button>
      </div>
    </form>
  );
};
