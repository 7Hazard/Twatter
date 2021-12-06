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

    return (
        <canvas id="whiteboard-canvas" width="1000" height="600"></canvas>
    )
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
                    eventHandlers.get(event)!(data)
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

            if(e.code == 1006) {
                // locally closed
                resolve(e)
            }
            else if(!ok) {
                reject(e)
            }
        }
    })
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

    // window.addEventListener('resize', resize);
    document.addEventListener('mousemove', draw);
    document.addEventListener('mousedown', setPosition);
    document.addEventListener('mouseenter', setPosition);

    // new position from mouse event
    function setPosition(e: MouseEvent) {
        let rect = canvas.getBoundingClientRect();
        pos.x = e.clientX - rect.left;
        pos.y = e.clientY - rect.top;
    }

    function draw(e: MouseEvent) {
        // mouse left button must be pressed
        if (e.buttons !== 1) return;

        ctx.beginPath(); // begin

        ctx.lineWidth = 5;
        ctx.lineCap = 'round';
        ctx.strokeStyle = '#000000';

        ctx.moveTo(pos.x, pos.y); // from
        setPosition(e);
        ctx.lineTo(pos.x, pos.y); // to

        ctx.stroke(); // draw it!
    }
}
