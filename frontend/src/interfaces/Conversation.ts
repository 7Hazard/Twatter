import { User } from "./User";

export interface Conversation {
    id: number
    name: string
    participants: User[]
}
