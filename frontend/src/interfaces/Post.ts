export interface Post {
    id: number
    content: string
    author: {
        id: number
        username: string
    },
    time: string
}