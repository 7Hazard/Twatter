import { Link, useParams } from "react-router-dom";
import { getFeed, getPost as getPosts } from "../../api";
import "./Feed.scoped.scss";

export default function () {
  const { username } = useParams();
  var data = username ? getPosts(username) : getFeed();
  
  return (
    <div className = "message-box">
      {username ? <h1>{username}'s feed</h1> : <h1>Your feed</h1>}
      <table>
        <thead>
          <tr>
            <th>Username</th>
            <th>Message</th>
          </tr>
        </thead>
        {data.map((users) => (
          <tbody>
            <tr>
              <td><Link to={`/user/${users.author.username}`}>{users.author.username}</Link></td>
              <td>{users.content}</td>
            </tr>
          </tbody>
        ))}
      </table>
    </div>
  );
}
