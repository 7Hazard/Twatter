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
import { deleteToken, isAuthenticated, setCookie } from "../../cookies";
import { authorizedGet, authorizedPost } from "../../api";
import Message from "../Message/Message";
import { Async } from "react-async";
import Status from "../Status/Status";
import { useState } from "react";
import Whiteboard from "../Whiteboard/Whiteboard";

export default function () {
    // order important, header must be after SignedRoutes (token check)
    return (
        <Router>
            <SignedRoutes />
            <Header />
        </Router>
    );
}

interface StatusResponse { missing: string[] }

function SignedRoutes() {
    const [status, setStatus] = useState(false);

    let location = useLocation();
    let token = new URLSearchParams(location.search).get("token");
    if (token) {
        // TODO validate with server for extra check
        // can happen that user puts in invalid token in query params
        setCookie("token", token as string, 1);
        window.history.pushState(null, "", "/");
    }

    if (isAuthenticated()) {
        return (<>
            {status && (
                <Routes>
                    <Route path="/" element={<Feed />} />
                    <Route path="/signout" element={<SignOut />} />
                    <Route path="/user/:username" element={<Feed />} />
                    <Route path="/messages" element={<Message />} />
                    <Route path="/messages/:conversationid/whiteboard" element={<Whiteboard />} />
                </Routes>
            )
            }
            <Async promise={authorizedGet("status")} >
                <Async.Pending>Loading...</Async.Pending>
                <Async.Rejected>{error => `${error.message}`}</Async.Rejected>
                <Async.Fulfilled<StatusResponse>>
                    {status => {
                        if (status.missing.length > 0) {
                            return <Status status={status} />
                        } else {
                            authorizedGet("self", true)
                                .then(user => setCookie("self", JSON.stringify(user), 1))
                                // .catch((e: Response) => alert("Could not get self: " + e.status))
                            setStatus(true)
                        }
                    }}
                </Async.Fulfilled>
            </Async>
        </>);
    }
    else return (
        <Routes>
            <Route path="/" element={<SignIn />} />
            <Route path="/auth/:token" element={<Auth />} />
            <Route path="/*" element={<Navigate to="/" />} />
        </Routes>
    );
}

function SignOut() {
    deleteToken();
    window.location.reload()
    return <></>
}