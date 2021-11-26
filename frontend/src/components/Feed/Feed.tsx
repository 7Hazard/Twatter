import React from "react";
import { useState } from "react";
import { Link, useParams } from "react-router-dom";
import { Async, useAsync } from "react-async"
import { fetchUserPosts, getFeed, getPost as getPosts, Post } from "../../api";
import "./Feed.scoped.css";

let fetched = false
export default function () {
  const [posts, setPosts] = React.useState<Post[]>([])

  const { username } = useParams();
  
  return (
    <div className="posts-container">
      <h1>
        {username ? `${username}'s feed` : `Your feed`}
      </h1>

      <label>
        <h2>Post:</h2>
        <input className="text-filed" type="text" name="name" />
      </label>
      <input className="submit-button" type="submit" value="Submit" />

      <h2>Feed:</h2>

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
                <p>&nbsp;·&nbsp;{(new Date().toLocaleString())}</p>
              </div>
              <p>{post.content}</p>
            </div>
          ))}
        </Async.Fulfilled>
      </Async>

    </div>
  );
}
