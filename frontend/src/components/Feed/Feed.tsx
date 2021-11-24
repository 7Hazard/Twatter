import { Link, useParams } from "react-router-dom";
import { getFeed, getPost as getPosts } from "../../api";
import "./Feed.scoped.scss";

export default function () {
  const { username } = useParams();
  var data = username ? getPosts(username) : getFeed();
  
  return (
    <div className = "posts-container">
      <h1>
        {username ? `${username}'s feed` : `Your feed`}
      </h1>
      
      {data.map((post) => (
        // Post container
        <div className="post-container">
          {/* Header container */}
          <div className="post-header-container">
            <Link to={`/user/${post.author.username}`}>{post.author.username}</Link>
            <p>&nbsp;·&nbsp;{(new Date().toLocaleString())}</p>
          </div>
          <p>{post.content}</p>
        </div>
      ))}

    </div>
  );
}
