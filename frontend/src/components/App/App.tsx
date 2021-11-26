import Header from "../Header/Header";
import "./App.scoped.css";

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
import { authorizedGet } from "../../api";
import Message from "../Message/Message";
import { Async } from "react-async";
import Status from "../Status/Status";

export default function () {
  // order important, header must be after SignedRoutes (token check)
  return (
    <Router>
      <SignedRoutes />
      <Header />
    </Router>
  );
}

function SignedRoutes() {
  let location = useLocation();
  let token = new URLSearchParams(location.search).get("token");
  if (token) {
    // TODO validate with server for extra check
    // can happen that user puts in invalid token in query params
    setCookie("token", token as string, 1);
    window.history.pushState(null, "", "/");
  }

  if(isAuthenticated()) return (<>
    <Async promise={authorizedGet("status")} >
        <Async.Pending>Loading...</Async.Pending>
        <Async.Rejected>{error => `${error.message}`}</Async.Rejected>
        <Async.Fulfilled<{missing: string[]}>>
            {status => {
                if(status.missing.length > 0)
                  return <Status status={status} />
                else (
                  <Routes>
                    <Route path="/" element={<Feed />} />
                    <Route path="/user/:username" element={<Feed />} />
                    <Route path="/messages" element={<Message />} />
                  </Routes>
                )
            }}
        </Async.Fulfilled>
    </Async>
  </>);
  else return (
    <Routes>
      <Route path="/" element={<SignIn />} />
      <Route path="/auth/:token" element={<Auth />} />
      <Route path="/*" element={<Navigate to="/" />} />
    </Routes>
  );
}
