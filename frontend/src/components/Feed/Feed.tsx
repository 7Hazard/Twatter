import { useParams } from "react-router-dom";
import { getFeed } from "../../api";
import "./Feed.scoped.scss";

export default function () {
  const { username } = useParams();
  var data = getFeed();

  return (
    <div className = "message-box">
      {username ? <h1>{username}'s feed</h1> : <h1>Your feed</h1>}
      <table>
        <thead>
          <tr>
            <th>Username</th>
            <th>Content</th>
          </tr>
        </thead>
        {data.map((users) => (
          <tbody>
            <tr>
              <td><a href={users.author.username}>{users.author.username}</a></td>
              <td>{users.content}</td>
            </tr>
          </tbody>
        ))}
      </table>
    </div>
  );
}
