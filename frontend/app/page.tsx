export default function Home() {
  return (
    <div className="flex justify-center items-center flex-col gap-8">
      <div className="flex flex-col lg:flex-row gap-8 p-2 w-5/6">
        <div className="bg-white dark:bg-slate-800 rounded-lg p-8 ring-1 ring-slate-900/5 shadow-xl flex-1">
          <h2 className="text-slate-900 dark:text-slate-100 text-2xl font-medium tracking-tight">
            Description
          </h2>
          <p className="text-slate-500 dark:text-slate-300 mt-4 py-1 text-justify">
            Implemented a Breadth-First-Search approach to Web Crawling. The
            goal of this task is to perform a web crawl on a URL string provided
            by the user. The application starts from an initial base URL and
            goes down to N levels (can be configured by the user). From the
            crawl, we parse out all the images on that web page and return a
            JSON array of strings that represent the URLs of all images on the
            page.
          </p>
        </div>
        <div className="bg-white dark:bg-slate-800 rounded-lg p-8 ring-1 ring-slate-900/5 shadow-xl flex-1">
          <h2 className="text-slate-900 dark:text-slate-100 text-2xl font-medium tracking-tight">
            How it works
          </h2>

          <ul className="text-slate-500 dark:text-slate-300 mt-4 list-disc px-4 text-justify">
            <li>
              Pressing the &ldquo;Try it out&rdquo; button on this page will route to the
              form page
            </li>

            <li>Submitting the form will make a POST request to /main</li>

            <li>
              That request will contain a form parameter with the url populated
              in the input box below
            </li>

            <li>
              The ImageFinder servlet will respond to the request with a list of
              image urls
            </li>

            <li>
              The /images page contains javascript to send the request and use
              the response to build a list of images
            </li>
          </ul>
        </div>
      </div>

      <button className="hover:bg-slate-800 bg-slate-950 dark:bg-slate-100 dark:hover:bg-slate-300 text-slate-200 hover:text-slate-100 dark:text-slate-950 dark:hover:text-slate-800 text-xl py-2 px-4 rounded">
        {`Try it out >>>`}
      </button>
    </div>
  );
}
