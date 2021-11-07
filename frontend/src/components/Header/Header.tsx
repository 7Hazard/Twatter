import { Link } from "react-router-dom";
import { getCookie, getToken, isAuthenticated } from "../../cookies";
import "./Header.scoped.scss";

export default function() {
  return (
    <header>
      <Link to="/"><img className="logo" src="/logo.gif" alt="Twatter Logo" /></Link>
      {isAuthenticated() ? (
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
