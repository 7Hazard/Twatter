import { getSuggestedQuery } from "@testing-library/dom"
import { deleteToken, getToken } from "./cookies"
import { useAsync } from "react-async"

export const api = `http://localhost:8080`

export interface Post {
    id: number
    content: string
    author: {
        id: number
        username: string
    }
  }

export async function getStatus() {
    let response = await fetch(`${api}/status`, {
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

