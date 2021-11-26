import React from "react";
import { useState } from "react";
import { Link, useParams } from "react-router-dom";
import { fetchUserPosts, getFeed, getPost as getPosts, Post } from "../../api";
import "./Feed.scoped.scss";
import { useAsync } from "react-async"

let fetched = false
export default function () {
  const [posts, setPosts] = React.useState<Post[]>([])

  const { username } = useParams();

  const { data, error, isPending } = useAsync({ promiseFn: fetchUserPosts, username: username })
  // if(username){
  //   fetchUserPosts(username)
  //   .then(v => {
  //     setPosts(v.data)
  //   })
  //   .catch(e => {
  //     alert(e);
  //   })
  }
  
  return (
    <div className = "posts-container">
      <h1>
        {username ? `${username}'s feed` : `Your feed`}
      </h1>

      <label>
          <h2>Post:</h2>
          <input className="text-filed" type="text" name="name" />
      </label>
        <input className="submit-button" type="submit" value="Submit" />

      <h2>Feed:</h2>
      {(posts).map((post) => (
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
