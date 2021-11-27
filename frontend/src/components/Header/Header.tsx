import { Link } from "react-router-dom";
import { searchUser } from "../../api";
import { getCookie, getToken, isAuthenticated } from "../../cookies";
import "./Header.scoped.css";

export default function() {
  return (
    <header>
      <Link to="/"><img className="logo" src="/logo.gif" alt="Twatter Logo" /></Link>
      
      {isAuthenticated() ? (<>
        <div className="search">
            <input type="text" placeholder="Search" id="SearchText"></input>
        </div>
        <nav>
          <Link to="/">Feed</Link>
          <Link to="/messages">Messages</Link>
          <Link to="/signout">Sign Out</Link>
        </nav>
      </>) : (
        <nav />
      )}
    </header>
  );
}
