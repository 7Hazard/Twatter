import { useParams } from "react-router-dom";
import { getFeed } from "../../api";
import "./Feed.scoped.scss";

export default function () {
  const { username } = useParams();
  var data = getFeed();

  return (
    //<div>{username ? <h1>{username}'s feed</h1> : <h1>Your feed</h1>}</div>
    //<h2>Username</h2> {data[0].author.username} 
    //<br />
    //<h2>Content</h2>  {data[0].content} 

    <div className = "message-box">
      <h1> Your feed </h1>
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
              <td>{users.author.username}</td>
              <td>{users.content}</td>
            </tr>
          </tbody>
        ))}
      </table>
    </div>
  );
}
