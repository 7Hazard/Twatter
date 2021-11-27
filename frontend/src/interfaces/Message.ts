import { User } from "./User";

export interface Message {
    id:number
    conversationId: number
    from: User
    content: string
    time: string
}