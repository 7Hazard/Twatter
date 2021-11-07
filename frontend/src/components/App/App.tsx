import Header from "../Header/Header";
import "./App.scoped.scss";

import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
  useLocation,
} from "react-router-dom";
import Feed from "../Feed/Feed";
import SignIn from "../Auth/Auth";
import Auth from "../Auth/Auth";
import { isAuthenticated, setCookie } from "../../cookies";

export default function() {
  // order important, header must be after SignedRoutes (token check)
  return (
    <Router>
      <SignedRoutes />
      <Header />
    </Router>
  );
}

function SignedRoutes() {
  // check if assigned new token, this can perhaps be done in a better way
  let location = useLocation();
  let token = new URLSearchParams(location.search).get("token");
  if (token) {
    // TODO validate with server for extra check
    // can happen that user puts in invalid token in query params
    setCookie("token", token as string, 1);
    window.history.pushState(null, "", "/");

    // TODO see if it's a new user, check his user status
  }

  if (isAuthenticated())
    return (
      <Routes>
        <Route path="/" element={<Feed />} />
        <Route path="/:username" element={<Feed />} />
      </Routes>
    );
  else {
    return (
      <Routes>
        <Route path="/" element={<SignIn />} />
        <Route path="/auth/:token" element={<Auth />} />
        <Route path="/*" element={<Navigate to="/" />} />
      </Routes>
    );
  }
}
