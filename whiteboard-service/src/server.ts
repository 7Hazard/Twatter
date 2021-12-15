import * as WebSocket from 'ws';
import { WebSocketServer } from 'ws';
import { connectDatabase } from './Database';
import { Whiteboard } from './Whiteboard';

await connectDatabase()

// Instantiate websocket server
const wss = new WebSocketServer({ port: 34343 });

let clientConversation = new Map<WebSocket, string>()
let conversationClients = new Map<string, Set<WebSocket>>()
function getClients(conversation: string = undefined): Set<WebSocket> {
    if (conversation && conversationClients.has(conversation)) return conversationClients.get(conversation)
    else if (!conversation) return wss.clients
    else return undefined
}

// Define event handlers
let eventHandlers = new Map<string, (client: WebSocket, data: any) => void>()

eventHandlers.set("mouse", (client, data) => {
    const convo = clientConversation.get(client)
    for (const otherClient of getClients(convo)) {
        if (otherClient != client)
            sendEvent("mouse", data, otherClient)
    }
})

eventHandlers.set("draw", (client, data) => {
    const convo = clientConversation.get(client)
    for (const otherClient of getClients(convo)) {
        if (otherClient != client)
            sendEvent("draw", data, otherClient)
    }
})

eventHandlers.set("fullupdate", async (client, data) => {
    const convo = clientConversation.get(client)
    for (const otherClient of getClients(convo)) {
        if (otherClient != client)
            sendEvent("fullupdate", data, otherClient)
    }
    await Whiteboard.upsert({ conversationId: convo, image: data.image })
})

interface SaveEvent { image: string }
eventHandlers.set("save", async (client, data: SaveEvent) => {
    const convo = clientConversation.get(client)
    await Whiteboard.upsert({ conversationId: convo, image: data.image })
    console.log("Saved whiteboard "+convo)

    // Pass all queued clients
    if (convoQueue.has(convo)) {
        let clients = convoQueue.get(convo)
        convoQueue.set(convo, [])
        for (const client of clients) {
            console.log(data.image)
            connectClient(convo, client, { image: data.image })
        }
    }
    console.log("Dequeued clients\n")
})

// Request save from each active whiteboard every 5s
setInterval(() => {
    for (const convo of conversationClients.keys()) {
        // let lastSave = Date.parse(lastSaves.get(convo)) < 
        // Check if less than 20s ago
        getSaveFromRandomClient(convo)
    }
}, 20000)

function getSaveFromRandomClient(convo: string) {
    console.log("Requesting latest save from "+convo)
    let clients = conversationClients.get(convo)
    let ok = false
    while (clients.size > 0 && !ok) {
        let rand = Math.floor(Math.random() * clients.size)
        let client = Array.from(clients)[rand]
        try {
            sendEvent("save", {}, client)
            ok = true
        } catch (error) {
            console.error("Client unreachable");
            disconnectClient(client)
        }
    }
}

let convoQueue = new Map<string, WebSocket[]>();

// Setup ws server
wss.on('connection', function connection(client) {
    client.on('message', function message(data) {
        try {
            let eventData = JSON.parse(data.toString())
            let event = eventData.event
            delete eventData.event

            if (event == "verify") {
                // TODO actually verify token
                const convo = eventData.conversationId
                queueClient(convo, client)
            }
            else {
                let handler = eventHandlers.get(event)
                if (handler)
                    handler(client, eventData)
                else console.error("Unhandled event: " + event)
            }
        } catch (error) {
            console.error("Error reading ws message, " + error)
        }
    });
    client.on("close", data => {
        console.log("Disconnected")
        const convo = clientConversation.get(client)
        const convoClients = conversationClients.get(convo)
        clientConversation.delete(client)
        convoClients.delete(client)
        if(convoClients.size == 0) conversationClients.delete(convo)
    })
});

function sendEvent(event: string, data: any, client: WebSocket) {
    data["event"] = event
    client.send(JSON.stringify(data))
}

interface State {
    image: string
}
function connectClient(convo: string, client: WebSocket, state: State) {
    clientConversation.set(client, convo)
    let convoclients = conversationClients.get(convo)
    if (!convoclients) {
        convoclients = new Set<WebSocket>()
        conversationClients.set(convo, convoclients)
    }
    convoclients.add(client)
    sendEvent("connected", state, client)
}

function disconnectClient(client: WebSocket) {
    const convo = clientConversation.get(client)
    const convoClients = conversationClients.get(convo)
    convoClients.delete(client)
    if (convoClients.size == 0) conversationClients.delete(convo)
    client.close() // TODO code
}

async function queueClient(convo: string, client: WebSocket) {
    let queue = convoQueue.get(convo)
    if (!queue || queue.length == 0) {
        // is only client connected, pass him through with latest state from db
        let whiteboard = (await Whiteboard.findOne({ where: { conversationId: convo } }))
        connectClient(convo, client, { image: whiteboard?.image })

        queue = []
        convoQueue.set(convo, queue)
        return
    }

    queue.push(client)
    getSaveFromRandomClient(convo)
}
