import { Chart } from "./Chart";

export interface Post {
    id: number
    content: string
    charts: Chart[]
    author: {
        id: number
        username: string
    },
    created: string
}