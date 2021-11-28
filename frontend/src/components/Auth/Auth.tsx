import { useState } from "react";
import { authorizedPost } from "../../api";
import { setCookie } from "../../cookies";
import "./Auth.scoped.css";

export default function () {
    let [loginError, setLoginError] = useState("");
    let [signupError, setSignupError] = useState("");

    return (<>
        <section>
            <form
                className="signin"
                onSubmit={(e) => {
                    e.preventDefault();
                    authorizedPost("auth/signin", {
                        username: e.currentTarget["username"].value,
                        password: e.currentTarget["password"].value
                    })
                        .then(result => {
                            setCookie("token", result.token, 1)
                            window.location.reload()
                        })
                        .catch((r:Response) => setLoginError(`Error: ${r.status}`))
                }}
            >
                <h1>Login</h1>
                <p>{loginError}</p>
                <a
                    className="button-link"
                    href="https://github.com/login/oauth/authorize?client_id=f370994586d726afd433"
                >
                    <img src="github.png" />
                    Login with Github
                </a>

                <div>
                    <hr />
                    <input type="text" placeholder="username" name="username" />
                    <input type="password" placeholder="password" name="password" />
                    <input type="submit" value="Sign In" />
                </div>
            </form>
            <form
                className="signup"
                onSubmit={(e) => {
                    e.preventDefault()
                    authorizedPost("user", {
                        username: e.currentTarget["username"].value,
                        password: e.currentTarget["password"].value
                    })
                        .then(result => {
                            setCookie("token", result.token, 1)
                            window.location.reload()
                        })
                        .catch((r:Response) => setSignupError(`Error: ${r.status}`))
                }}
            >
                <h1>Sign up</h1>
                <p>{signupError}</p>
                <input type="text" placeholder="Username" name="username" />
                <input type="password" placeholder="password" name="password" />
                <input type="submit" value="Sign Up" />
            </form>
        </section>
    </>);
}
