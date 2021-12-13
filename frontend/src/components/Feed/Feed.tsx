import { Link, useNavigate, useParams } from "react-router-dom";
import { Async } from "react-async"
import { authorizedGet, authorizedPost, createPost, fetchUserPosts } from "../../api";
import "./Feed.scoped.css";
import { Post } from "../../interfaces/Post";
import { getSelf } from "../../cookies";
import { User } from "../../interfaces/User";
import { useState } from "react";
import Chart from "../Chart/Chart";

export default function () {
    const { username } = useParams();
    let navigate = useNavigate()

    let self = getSelf()

    const [toggle, setToggle] = useState(false)

    return (
        <div className="posts-container">
            <h1>{username ? `${username}` : `Your feed`}</h1>

            {username && username != self.username && (<>

                <FollowButton username={username} self={self} />

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
            </>)}

            {!username && (<>
                <input id="postText" className="text-filed" type="text" name="text" placeholder="What are you doing now?" />
                <input className="submit-button" type="submit" value="Post" onClick={
                    () => createPost((document.getElementById("postText") as HTMLInputElement).value).then(_ => setToggle(!toggle))
                } />
            </>)}

            <Async promise={username ? fetchUserPosts(username as string) : authorizedGet("feed", true)} >
                <Async.Pending></Async.Pending>
                <Async.Rejected>
                    Could not fetch posts
                    {error => console.log(error)}
                </Async.Rejected>
                <Async.Fulfilled<Post[]>>
                    {posts => (
                        <div className="posts-scroll">
                            {posts.map(post => <SinglePost post={post} />)}
                        </div>
                    )}
                </Async.Fulfilled>
            </Async>

        </div>
    );
}

function SinglePost({ post }: { post: Post }) {
    return (
        // Post container
        <div className="post-container">
            {/* Header container */}
            <div className="post-header-container">
                <Link to={`/user/${post.author.username}`}>{post.author.username}</Link>
                <p>&nbsp;·&nbsp;{(new Date(post.created).toLocaleString())}</p>
            </div>
            <p>{post.content}</p>
            {post.charts.map(chart => (
                <div style={{backgroundColor: "whitesmoke", padding:"0.5em", marginTop:"1em"}}>
                    {chart && <Chart data={chart.data} /> || "Invalid chart data"}
                </div>
            ))}
        </div>
    )
}

function FollowButton({ username, self }: { username: string, self: User }) {
    const [toggle, setToggle] = useState(false)

    return (
        <Async promise={authorizedGet(`user/${username}/followers`)} key={""}  >
            <Async.Pending></Async.Pending>
            <Async.Rejected>{error => `${error.message}`}</Async.Rejected>
            <Async.Fulfilled<User[]>>
                {users => (
                    <input type="submit" value={users.find(u => u.id == self.id) ? "Unfollow" : "Follow"} onClick={e => {
                        authorizedPost(`user/${username}/followers`)
                            .then(v => setToggle(!toggle))
                            .catch(e => alert("Could not toggle follow"))
                    }} />
                )}
            </Async.Fulfilled>
        </Async>
    )
}
