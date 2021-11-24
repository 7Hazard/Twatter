import React from "react";
import { Link, useParams } from "react-router-dom";
import { fetchUserPosts, getFeed, getPost as getPosts, Post } from "../../api";
import "./Feed.scoped.scss";

export default function () {

  const [posts, setPosts] = React.useState<Post>()

  const { username } = useParams();
  let data = username ? getUserPosts(username) : getFeed();

  if(username){
    fetchUserPosts(username)
    .then(v => {
      setPosts(v.data)
    })
  }
  
  return (
    <div className = "posts-container">
      <h1>
        {username ? `${username}'s feed` : `Your feed`}
      </h1>
      
      {posts.map(post => (
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
function getUserPosts(username: string) {
  throw new Error("Function not implemented.");
}

