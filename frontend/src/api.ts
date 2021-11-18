import { deleteToken, getToken } from "./cookies"

export const api = `http://localhost:8080`

export async function getStatus() {
    let response = await fetch(`${api}/user/status`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${getToken()}`
        }
    })

    if(!response.ok)
    {
        // Invalid credentials
        if(response.status == 401)
        {
            deleteToken()
            window.location.reload()
        }
        console.error(response);
        return { status: response.status, data: null }
    }

    let json = await response.json()

    return { status: response.status, data: json }
}
