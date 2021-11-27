import { Link, useNavigate, useParams } from "react-router-dom";
import { Async } from "react-async"
import { authorizedPost, createPost, fetchUserPosts } from "../../api";
import "./Feed.scoped.css";
import { Post } from "../../interfaces/Post";

let fetched = false
export default function () {
  const { username } = useParams();
  let navigate = useNavigate()

  return (
    <div className="posts-container">
      <h1>{username ? `${username}'s feed` : `Your feed`}</h1>

      {username && (
        <form className="send-message" onSubmit={e => {
          e.preventDefault()
          authorizedPost(`user/${username}/message`, {
            content: e.currentTarget["message"].value
          }, true)
            .then(v => {
              navigate("/messages")
            })
            .catch(r => {
              alert("Could not send message")
            })
        }}>
          <input type="text" placeholder="Message" name="message" />
          <input type="submit" value="Send" />
        </form>
      )}

      {!username && (<>
        <input id="postText" className="text-filed" type="text" name="text" placeholder="What are you doing?" />
        <input className="submit-button" type="submit" value="Post" onClick={() => createPost((document.getElementById("postText") as HTMLInputElement).value)} />
      </>)}

      <Async promise={fetchUserPosts(username as string)} >
        <Async.Pending>Loading...</Async.Pending>
        <Async.Rejected>{error => `${error.message}`}</Async.Rejected>
        <Async.Fulfilled>
          {(posts: Post[]) => posts.map(post => (
            // Post container
            <div className="post-container">
              {/* Header container */}
              <div className="post-header-container">
                <Link to={`/user/${post.author.username}`}>{post.author.username}</Link>
                <p>&nbsp;·&nbsp;{(new Date(post.time).toLocaleString())}</p>
              </div>
              <p>{post.content}</p>
            </div>
          ))}
        </Async.Fulfilled>
      </Async>

    </div>
  );
}
