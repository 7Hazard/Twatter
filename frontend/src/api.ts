import { deleteToken, getToken } from "./cookies"

export const api = `http://localhost:8080`

export async function getStatus() {
    let response = await fetch(`${api}/user/status`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${getToken()}`
        }
    })

    if (!response.ok) {
        // Invalid credentials
        if (response.status == 401) {
            deleteToken()
            window.location.reload()
        }
        console.error(response);
        return { status: response.status, data: null }
    }

    let json = await response.json()

    return { status: response.status, data: json }
}

let fakedata = `
[
    {
        "id": 6,
        "content": "Hello my guys",
        "author": {
            "id": 4,
            "username": "popularguy",
            "name": null,
            "githubId": null
        }
    },
    {
        "id": 3,
        "content": "Whats up broski",
        "author": {
            "id": 6,
            "username": "Mill3nium",
            "name": null,
            "githubId": null
        }
    }
]
`

export function getFeed(): [{
    id: number, content: string, author: {
        id: number
        username: string
    }
}]
{
    return JSON.parse(fakedata)
}

let fakePosts = `
[
    {
        "id": 6,
        "content": "Hello my guys",
        "author": {
            "id": 4,
            "username": "popularguy",
            "name": null,
            "githubId": null
        }
    }
]
`

export function getPost(username:string): [{
    id: number, content: string, author: {
        id: number
        username: string
    }
}]
{
    return JSON.parse(fakePosts);
}

