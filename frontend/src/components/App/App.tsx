import Header from "../Header/Header";
import "./App.scoped.scss";

import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import Feed from "../Feed/Feed";
import SignIn from "../SignIn/SignIn";

export default function() {
  return (
    <BrowserRouter>
      <Header />
      <SignedRoutes />
    </BrowserRouter>
  );
}

function SignedRoutes() {
  let signedIn = false;
  if (signedIn)
    return (
      <Routes>
        <Route path="/" element={<Feed />} />
        <Route path="/:username" element={<Feed />} />
      </Routes>
    );
  else
    return (
      <Routes>
        <Route path="/" element={<SignIn />} />
        <Route path="/:username" element={<Navigate to="/" />} />
      </Routes>
    );
}
