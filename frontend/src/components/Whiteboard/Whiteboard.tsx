import { useEffect } from "react";
import { Async } from "react-async";
import { getToken } from "../../cookies";
import "./Whiteboard.scoped.css"

export default function () {
    return (<>
        <div className="whiteboard-container">
            <Async promise={connect()} key={""} >
                <Async.Pending></Async.Pending>
                <Async.Rejected>{error => `${error.message}`}</Async.Rejected>
                <Async.Fulfilled>
                    {_ => Whiteboard()}
                </Async.Fulfilled>
            </Async>
        </div>
    </>);
}

function Whiteboard() {
    useEffect(() => {
        setupCanvas()
    })

    return (<>
        <canvas id="whiteboard-canvas" width="1000" height="600"></canvas>
        <canvas id="mouse-canvas" width="1000" height="600"></canvas>
    </>)
}

let eventHandlers = new Map<string, (data: any) => void>()
let ws: WebSocket
let connectionPromise: Promise<unknown>
function connect() {
    return connectionPromise != undefined ? connectionPromise : connectionPromise = new Promise((resolve, reject) => {
        ws = new WebSocket("ws://localhost:34343");
        let ok = false
        ws.onopen = e => {
            ws.send(JSON.stringify({
                event: "verify",
                token: getToken()
            }))
            console.log("Verifying");
        }
        ws.onmessage = m => {
            try {
                let data = JSON.parse(m.data)
                let event = data.event
                delete data.event

                if(event == "connected") {
                    console.log("Verified");
                    ok = true
                    return resolve(data)
                }
                else {
                    let handler = eventHandlers.get(event)
                    if(handler) handler(data)
                    else console.error("Unhandled event: "+event)
                }
            } catch (error) {
                console.error("Could not process ws message:", error)
                if(!ok) reject(error)
            }
        }
        ws.onerror = e => {
            console.error("Error in ws:", e);
        }
        ws.onclose = e => {
            console.log("ws closed:", e)
            // if(e.code == 1006) {
            //     // locally closed
            //     resolve(e)
            // }
            // else 
            if(!ok) {
                reject(e)
            }
        }
    })
}

function sendEvent(event: string, data: any) {
    data["event"] = event
    ws.send(JSON.stringify(data))
}

function setupCanvas() {
    // https://stackoverflow.com/questions/2368784/draw-on-html5-canvas-using-a-mouse/30684711#30684711
    let canvas = document.getElementById("whiteboard-canvas") as HTMLCanvasElement;

    if(!canvas) {
        console.error("Could not find whiteboard canvas");
        return
    } else console.log("Found whiteboard canvas");

    // get canvas 2D context and set him correct size
    let ctx = canvas.getContext('2d') as CanvasRenderingContext2D;

    // last known position
    let pos = { x: 0, y: 0 };

    canvas.addEventListener('mousemove', e => {

        let fromX = pos.x, fromY = pos.y
        setPosition(e)
        sendEvent("mouse", {x: pos.x, y: pos.y})

        // mouse left button must be pressed
        if (e.buttons !== 1) return;

        let toX = pos.x, toY = pos.y
        draw(fromX, fromY, toX, toY)
        sendEvent("draw", {fromX, fromY, toX, toY})
    })

    document.addEventListener('mousemove', e => {
        
    });
    document.addEventListener('mousedown', e => {
        setPosition(e)
        draw(pos.x, pos.y, pos.x, pos.y)
        sendEvent("draw", {fromX: pos.x, fromY: pos.y, toX: pos.x, toY: pos.y})
    });
    document.addEventListener('mouseenter', setPosition);

    // new position from mouse event
    function setPosition(e: MouseEvent) {
        let rect = canvas.getBoundingClientRect();
        pos.x = e.clientX - rect.left;
        pos.y = e.clientY - rect.top;
    }

    function draw(fromX: number, fromY: number, toX: number, toY: number) {
        ctx.beginPath(); // begin

        ctx.lineWidth = 5;
        ctx.lineCap = 'round';
        ctx.strokeStyle = '#000000';

        ctx.moveTo(fromX, fromY); // from
        ctx.lineTo(toX, toY); // to

        ctx.stroke(); // draw it!
    }

    eventHandlers.set("draw", data => {
        draw(data.fromX, data.fromY, data.toX, data.toY)
    })

    let mouseImage = new Image()
    mouseImage.src = `https://freesvg.org/img/Mouse-Cursor-Pointer.png`
    let mousectx = (document.getElementById("mouse-canvas") as HTMLCanvasElement).getContext('2d') as CanvasRenderingContext2D
    eventHandlers.set("mouse", data => {
        mousectx.clearRect(0, 0, 1000, 600)
        // TODO draw all mice
        mousectx.drawImage(mouseImage, data.x-10, data.y-5, 25, 25)
    })
}
