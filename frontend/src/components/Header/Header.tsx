import { Link } from "react-router-dom";
import "./Header.scoped.scss";

export default function() {
  let signedIn = false;

  return (
    <header>
      <Link to="/"><img className="logo" src="/logo.gif" alt="Twatter Logo" /></Link>
      {signedIn ? (
        <nav>
          <Link to="/feed">Feed</Link>
          <Link to="/signout">Sign Out</Link>
        </nav>
      ) : (
        <nav />
      )}
    </header>
  );
}
