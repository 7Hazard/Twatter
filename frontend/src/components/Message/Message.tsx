import { Link } from "react-router-dom";
import { getCookie, getToken, isAuthenticated } from "../../cookies";
import "./Message.scoped.css";

export default function () {
  return (
    <div>
      <h1>Send message</h1>
      <form>
      <label>
          <h2>To:</h2>
          <input className="text-filed" type="text" name="name" />
        </label>
        <label>
          <h2>Message:</h2>
          <input className="text-filed" type="text" name="name" />
        </label>
        <input className="submit-button" type="submit" value="Submit" />
      </form>
    </div>
  );
}


