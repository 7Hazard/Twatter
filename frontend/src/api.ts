import { deleteToken, getToken } from "./cookies"

export const api = `http://localhost:8080`

export interface Post {
id: number
    content: string
    author: {
        id: number
        username: string
    }
}

export async function authorizedPost(path: string, body = {}, invalidateToken = false) {
    let response = await fetch(`${api}/${path}`, {
        method: "post",
        headers: {
            "Authorization": `Bearer ${getToken()}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify(body)
    })

    if (!response.ok) {
        // Invalid credentials
        if (response.status == 401 && invalidateToken) {
            deleteToken()
            window.location.reload()
        }
        alert(response.status)
        throw response
    }

    try {
        return await response.json()
    } catch (error) {
        return {}
    }
}

export async function authorizedGet(path: string, clearCookies = false) {
    let response = await fetch(`${api}/${path}`, {
        method: "get",
        headers: {
            "Authorization": `Bearer ${getToken()}`,
        },
    })

    if (!response.ok) {
        // Invalid credentials
        if (response.status == 401 && clearCookies) {
            deleteToken()
            window.location.reload()
        }
        throw response
    }

    try {
        return await response.json()
    } catch (error) {
        return {}
    }
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

export function getFeed(): [Post] {
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

export function getPost(username: string): [Post] {
    return JSON.parse(fakePosts);
}

export async function fetchUserPosts(username:string): Promise<Post[]> {
    let response = await fetch(`${api}/user/${username}/posts`, {
        method: "GET",
        headers: {
            "Authorization": `Bearer ${getToken()}`
        }
    })

    if (!response.ok) {
        throw Error(response.statusText)
    }
    
    let json = await response.json()
    return json
}

