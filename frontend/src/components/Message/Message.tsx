import { useState } from "react";
import { Async } from "react-async";
import { authorizedGet, authorizedPost, uploadImage } from "../../api";
import { getSelf } from "../../cookies";
import { Conversation } from "../../interfaces/Conversation";
import { Message } from "../../interfaces/Message";
import "./Message.scoped.css";
import { Link } from "react-router-dom";

export default function () {
    const [selectedConvo, setSelectedConvo] = useState<Conversation>(undefined!);
    const [toggle, setToggle] = useState(false)
    let self = getSelf()

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
                                <div className={`convo ${selectedConvo && convo.id == selectedConvo.id && "convo-selected"}`} onClick={e => {
                                    setSelectedConvo(convo)
                                }}>
                                    <h3>{convo.name}</h3>
                                </div>
                            )
                        })}
                    </Async.Fulfilled>
                </Async>
            </div>
            
        <div className="message-view">
            {selectedConvo && (<>
                <div className="messages-title-container">
                    <h1>{selectedConvo.name}</h1>
                </div>
                <div className="message-view-content">
                    <Async promise={authorizedGet(`conversations/${selectedConvo.id}/messages`)} >
                        <Async.Pending></Async.Pending>
                        <Async.Rejected>{error => `${error.message}`}</Async.Rejected>
                        <Async.Fulfilled<Message[]>>
                            {messages => (messages = messages.slice(0).reverse()).map((msg, i) => (
                                <div className={`message ${msg.from.id == self.id ? "message-sent" : "message-received"}`}>
                                    <div>
                                        <div className="message-content">
                                            {msg.image ? <img src={msg.content} /> : msg.content}
                                        </div>
                                        {(i == 0 || msg.from.id != messages[i - 1].from.id) && (
                                            <Link to={`/user/${msg.from.username}`}>{msg.from.username}</Link>
                                        )}
                                    </div>
                                </div>
                            ))}
                        </Async.Fulfilled>
                    </Async>
                </div>
                <form className="message-send-container" autoComplete="off" onSubmit={e => {
                    e.preventDefault()
                    let content = e.currentTarget["msg"].value;
                    if (content == "" || content == null) return
                    authorizedPost(`conversations/${selectedConvo.id}/messages`, { content }, true)
                        .then(result => setToggle(!toggle))
                        .catch(resp => alert(resp.status))
                    e.currentTarget["msg"].value = ""
                }}>
                    <label className="message-upload-image">
                        <input type="file" onInput={async e => {
                            alert("image")
                            let filebase64 = await fileToBase64(e.currentTarget.files![0])
                            try {
                                await authorizedPost(`conversations/${selectedConvo.id}/messages`, {
                                    content: filebase64,
                                    image: true
                                }, true)
                                setToggle(!toggle);
                            } catch (e) {
                                alert("Could not upload image")
                            }
                        }} />
                        🏞️
                    </label>
                    <input name="msg" placeholder="Write your message here" />
                    <input className="message-send-button" type="submit" value="Send" />
                </form>
            </>)}
        </div>
        </div>
    </>);
}

function fileToBase64(file: File) {
    return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = () => {
            let encoded = reader.result!.toString().replace(/^data:(.*,)?/, '');
            if ((encoded.length % 4) > 0) {
                encoded += '='.repeat(4 - (encoded.length % 4));
            }
            resolve(encoded);
        };
        reader.onerror = error => reject(error);
    });
}
