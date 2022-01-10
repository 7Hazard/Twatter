import { deleteToken, getToken } from "./cookies"
import { Post } from "./interfaces/Post"

export const api = `https://localhost:8080`

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

export async function fetchUserPosts(username: string): Promise<Post[]> {
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

export async function createPost(post: string) {
    let resp = await fetch(`${api}/post`, {
        method: "POST",
        headers: {
            "Authorization": `Bearer ${getToken()}`,
            "Content-Type": "application/json"
        },
        body: JSON.stringify({ "content": post })
    })
}

export async function searchUser(username: string) {
    alert(username)
    let response = await fetch(`${api}/search/user/${username}`, {
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

export async function uploadImage(file: File, convoid: number) {
    let filebase64: string = ""
    try {
        filebase64 = await fileToBase64(file) as string
    } catch (e) {
        console.error("failed base64")
        throw e
    }

    let resp = await authorizedPost(`conversations/${convoid}/messages`, {
        content: filebase64,
        image: true
    }, true)

    if (!resp.ok) {
        console.error(await resp.json())
        throw resp
    }

    return resp
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