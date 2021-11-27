import { useState } from "react";
import { Async } from "react-async";
import { authorizedGet, authorizedPost } from "../../api";
import { getSelf } from "../../cookies";
import { Conversation } from "../../interfaces/Conversation";
import { Message } from "../../interfaces/Message";
import "./Message.scoped.css";

export default function () {
    const [convo, setConvo] = useState<Conversation>(undefined);
    const [toggle, setToggle] = useState(false)

    return (<>
        <div className="messages">
            <div className="users-list-container">
                <div className="messages-title-container">
                    <h1>Messages</h1>
                </div>
                <Async promise={authorizedGet("conversations")} >
                    <Async.Pending></Async.Pending>
                    <Async.Rejected>{error => `${error.message}`}</Async.Rejected>
                    <Async.Fulfilled<Conversation[]>>
                        {convos => convos.map(convo => {
                            if (!convo.name) {
                                convo.name = convo.participants
                                    .filter(u => u.id != getSelf().id)
                                    .map(u => u.username)
                                    .join(", ")
                            }

                            return (
                                <div className="user" onClick={e => {
                                    setConvo(convo)
                                }}>
                                    <h3>{convo.name}</h3>
                                </div>
                            )
                        })}
                    </Async.Fulfilled>
                </Async>
            </div>
            <div className="message-view">
                {convo && (<>
                    <div className="messages-title-container">
                        <h1>{convo.name}</h1>
                    </div>
                    <div className="message-view-content">
                        <Async promise={authorizedGet(`conversations/${convo.id}/messages`)} >
                            <Async.Pending></Async.Pending>
                            <Async.Rejected>{error => `${error.message}`}</Async.Rejected>
                            <Async.Fulfilled<Message[]>>
                                {messages => {
                                    let self = getSelf()
                                    return messages.slice(0).reverse().map(msg => (
                                        <div className={msg.from.id == self.id ? "message-sent" : "message-received"}>
                                            <p>{msg.content}</p>
                                        </div>
                                    ))
                                }}
                            </Async.Fulfilled>
                        </Async>
                    </div>
                    <form className="message-send-container" onSubmit={e => {
                        e.preventDefault()
                        let content = e.currentTarget["msg"].value;
                        authorizedPost(`conversations/${convo.id}/messages`, { content }, true)
                            .then(result => setToggle(!toggle))
                            .catch(resp => alert(resp.status))
                    }}>
                        <input name="msg" placeholder="Write your message here" />
                        <input className="message-send-button" type="submit" value="Send" />
                    </form>
                </>)}
            </div>
        </div>
    </>);
}
