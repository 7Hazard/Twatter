import { Link } from "react-router-dom";
import { searchUser } from "../../api";
import { getCookie, getToken, isAuthenticated } from "../../cookies";
import "./Header.scoped.css";

export default function() {
  return (
    <header>
      <Link to="/"><img className="logo" src="/logo.gif" alt="Twatter Logo" /></Link>
      {isAuthenticated() ? (
        <nav>
          <div className="navbar">
          <Link to="/">Feed</Link>
          <Link to="/signout">Sign Out</Link>
          <Link to="/messages">Message</Link>
          </div>
          <div className="search-element">
            <input type="text" placeholder="Search.." id="SearchText"></input>
            <button type="submit" onClick={()=>searchUser((document.getElementById("SearchText") as HTMLInputElement).value)} >Search</button>
          </div>
        </nav>
      ) : (
        <nav />
      )}
    </header>
  );
}
