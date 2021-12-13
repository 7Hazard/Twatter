import * as WebSocket from 'ws';
import { WebSocketServer } from 'ws';

// Instantiate websocket server
const wss = new WebSocketServer({ port: 34343 });

// Define event handlers
let eventHandlers = new Map<string, (client: WebSocket, data: any) => void>()

eventHandlers.set("mouse", (client, data) => {
    for (const otherClient of wss.clients) {
        if(otherClient != client)
            sendEvent("mouse", data, otherClient)
    }
})

eventHandlers.set("draw", (client, data) => {
    // TODO sent mouse pos of all mice
    for (const otherClient of wss.clients) {
        if(otherClient != client)
            sendEvent("draw", data, otherClient)
    }
})

eventHandlers.set("fullupdate", (client, data) => {
    for (const otherClient of wss.clients) {
        if(otherClient != client)
            sendEvent("fullupdate", data, otherClient)
    }
})

// Setup ws server
wss.on('connection', function connection(client) {
  client.on('message', function message(data) {
      try {
        let eventData = JSON.parse(data.toString())
        let event = eventData.event
        delete eventData.event

        if(event == "verify") {
            // TODO actually verify
            sendEvent("connected", {}, client)
        }
        else {
            let handler = eventHandlers.get(event)
            if(handler)
                handler(client, eventData)
            else console.error("Unhandled event: "+event)
        }
      } catch (error) {
          console.error("Error reading ws message, "+error)
      }
  });
});

function sendEvent(event: string, data: any, client: WebSocket) {
    data["event"] = event
    client.send(JSON.stringify(data))
}
