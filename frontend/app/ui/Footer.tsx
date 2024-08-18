const date = new Date();
const year = date.getFullYear();

const Footer = () => {
  return (
    <div className="flex flex-col items-center justify-center opacity-85 min-h-[15vh] gap-4 p-4 w-[80vw] border-t border-slate-900/40 dark:border-slate-300/10">
      <h3>Copyright © {year} Rishav Roy</h3>
      <h3>All Rights Reserved</h3>
    </div>
  );
};

export default Footer;
