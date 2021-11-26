import { Link } from "react-router-dom";
import { getCookie, getToken, isAuthenticated } from "../../cookies";
import "./Header.scoped.css";

export default function() {
  return (
    <header>
      <Link to="/"><img className="logo" src="/logo.gif" alt="Twatter Logo" /></Link>
      {isAuthenticated() ? (
        <nav>
          <Link to="/">Feed</Link>
          <Link to="/signout">Sign Out</Link>
          <Link to="/messages">Message</Link>
        </nav>
      ) : (
        <nav />
      )}
    </header>
  );
}
