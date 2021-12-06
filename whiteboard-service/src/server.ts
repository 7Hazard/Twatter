import * as WebSocket from 'ws';
import { WebSocketServer } from 'ws';

// Instantiate websocket server
const wss = new WebSocketServer({ port: 34343 });

// Define event handlers
let eventHandlers = new Map<string, (ws: WebSocket, event: any) => void>()






// Setup ws server
wss.on('connection', function connection(ws) {
  ws.on('message', function message(data) {
      try {
        let eventData = JSON.parse(data.toString())
        let event = eventData.event
        delete eventData.event

        if(event == "verify") {
            // TODO actually verify
            ws.send(JSON.stringify({event: "connected"}))
        }
        else {
            eventHandlers.get(event)!(ws, eventData)
        }
      } catch (error) {
          console.error("Error reading ws message, "+error)
      }
  });
});
