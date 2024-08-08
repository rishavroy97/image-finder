const date = new Date();
const year = date.getFullYear();

const Footer = () => {
  return (
    <div className="flex flex-col items-center justify-center opacity-85 min-h-[10vh]">
      <h3>Copyright © {year} Rishav Roy</h3>
      <h3>All Rights Reserved</h3>
    </div>
  );
};

export default Footer;
