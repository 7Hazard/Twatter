import { useState } from "react";
import { Async } from "react-async";
import { Navigate, useNavigate } from "react-router";
import { authorizedPost } from "../../api";
import "./Status.scoped.css"

interface Status {
    missing: string[]
}

export default function({status}: {status: Status}) {
    let navigate = useNavigate()
    
    const [username, setUsername] = useState<string>("");
    const [name, setName] = useState<string>("");

    return (<>
        <form onSubmit={async (e) => {
            e.preventDefault()
            if(username!.length < 1 && name!.length < 1)
            {
                alert("Fill it in")
                return;
            }

            try {
                let result = await authorizedPost("details", {username, name}, true)
            } catch (e: any) {
                alert(JSON.stringify(e))
            }
            
            navigate("/")
        }}>
            { status.missing.includes("username") && <input type="text" placeholder="Username" onChange={e => setUsername(e.target.value)} /> }
            { status.missing.includes("name") && <input type="text" placeholder="Name" onChange={e => setName(e.target.value)} /> }
            <input type="submit" />
        </form>
    </>)
}
