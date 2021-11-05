import { useParams } from "react-router-dom";
import "./Feed.scoped.scss";

export default function() {
  const { username } = useParams();

  return (
    <div>{username ? <h1>{username}'s feed</h1> : <h1>Your feed</h1>}</div>
  );
}
