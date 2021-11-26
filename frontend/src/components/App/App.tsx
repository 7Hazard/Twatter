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
import { getStatus } from "../../api";
import Message from "../Message/Message";
import { Async, IfFulfilled, IfPending, IfRejected, useAsync } from "react-async";

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
    <Routes>
      <Route path="/" element={<Feed />} />
      <Route path="/user/:username" element={<Feed />} />
      <Route path="/messages" element={<Message />} />
    </Routes>
    <Async promise={getStatus()} >
      <Async.Pending>Loading...</Async.Pending>
      <Async.Rejected>{error => `${error.message}`}</Async.Rejected>
      <Async.Fulfilled>
        {/* {data => (
          <p>{data}</p>
        )} */}
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

// function SignedRoutes2() {
//   const [status, setStatus] = React.useState<any>()

//   // check if assigned new token, this can perhaps be done in a better way
//   let location = useLocation();
//   let token = new URLSearchParams(location.search).get("token");
//   if (token) {
//     // TODO validate with server for extra check
//     // can happen that user puts in invalid token in query params
//     setCookie("token", token as string, 1);
//     window.history.pushState(null, "", "/");
//   }

//   if (isAuthenticated() && status === undefined) {
//     setStatus(null)
//     getStatus().then(s => setStatus(s.data))
//     return null
//   }

//   if (isAuthenticated()) {
//     return (
//       <Routes>
//         <Route path="/" element={<Feed />} />
//         <Route path="/user/:username" element={<Feed />} />
//         <Route path="/messages" element={<Message />} />
//       </Routes>
//     );
//   }
//   else {
//     return (
//       <Routes>
//         <Route path="/" element={<SignIn />} />
//         <Route path="/auth/:token" element={<Auth />} />
//         <Route path="/*" element={<Navigate to="/" />} />
//       </Routes>
//     );
//   }
// }
