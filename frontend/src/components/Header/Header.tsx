import { useState } from "react";
import { Async } from "react-async";
import { Link } from "react-router-dom";
import { authorizedGet } from "../../api";
import { isAuthenticated } from "../../cookies";
import { User } from "../../interfaces/User";
import "./Header.scoped.css";

export default function () {


    return (
        <header>
            <Link to="/"><img className="logo" src="/logo.gif" alt="Twatter Logo" /></Link>

            {isAuthenticated() ? (<>
                <SearchBar />
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

function SearchBar() {
    const [focused, setFocused] = useState(false)
    const [search, setSearch] = useState("")

    return (
        <div className="search">
            <input type="text" placeholder="Search" onChange={e => {
                setSearch(e.currentTarget.value)
            }} onFocus={e => setFocused(true)} onBlur={e => setTimeout(()=>setFocused(false), 100)} />

            {search.length > 0 && focused && (
                <div className="search-results">
                    <Async promise={authorizedGet(`search/users/${search}`)} >
                        <Async.Pending></Async.Pending>
                        <Async.Rejected>{(e: Response) => `No results: ${e.status}`}</Async.Rejected>
                        <Async.Fulfilled<User[]>>
                            {users => users.map(u => (
                                <Link to={"/user/" + u.username}>{u.username}</Link>
                            ))}
                        </Async.Fulfilled>
                    </Async>
                </div>
            )}
        </div>
    )
}
