import "./Auth.scoped.css";

export default function() {
  //   let [error, setError] = React.useState("");

  return (
    <section>
      <form
        className="signin"
        // onSubmit={(event) => {
        //   event.preventDefault();
        //   setError("Error");
        //   console.log("sign in");
        // }}
      >
        <h1>Login</h1>
        {/* <p>{error}</p>
        <input type="email" placeholder="email" />
        <input type="password" placeholder="password" />
        <input type="submit" value="Sign In" /> */}
        <a
          className="button-link"
          href="https://github.com/login/oauth/authorize?client_id=f370994586d726afd433"
        >
          <img src="github.png" />
          Login with Github
        </a>
      </form>
      {/* <form
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
      </form> */}
    </section>
  );
}
