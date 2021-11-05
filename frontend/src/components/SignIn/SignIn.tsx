import React from "react";
import "./SignIn.scoped.scss";

export default function() {
  let [error, setError] = React.useState("");

  return (
    <section>
      <form
        className="signin"
        onSubmit={(event) => {
          event.preventDefault();
          setError("Error")
          console.log("sign in");
        }}
      >
        <h1>Sign in</h1>
        <p>{error}</p>
        <input type="email" placeholder="email" />
        <input type="password" placeholder="password" />
        <input type="submit" value="Sign In" />
      </form>
      <form
        className="signup"
        onSubmit={(event) => {
          console.log("sign up");
        }}
      >
        <h1>Sign up</h1>
        <input type="text" placeholder="name" />
        <input type="email" placeholder="email" />
        <input type="text" placeholder="username" />
        <input type="password" placeholder="password" />
        <input type="submit" value="Sign Up" />
      </form>
    </section>
  );
}
