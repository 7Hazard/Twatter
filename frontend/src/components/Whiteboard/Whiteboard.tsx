import { useEffect } from "react";
import "./Whiteboard.scoped.css"

export default function () {

    useEffect(() => {
        // https://stackoverflow.com/questions/2368784/draw-on-html5-canvas-using-a-mouse/30684711#30684711
        var canvas = document.getElementById("whiteboard-canvas") as HTMLCanvasElement;

        // get canvas 2D context and set him correct size
        var ctx = canvas.getContext('2d') as CanvasRenderingContext2D;

        // last known position
        var pos = { x: 0, y: 0 };

        // window.addEventListener('resize', resize);
        document.addEventListener('mousemove', draw);
        document.addEventListener('mousedown', setPosition);
        document.addEventListener('mouseenter', setPosition);

        // new position from mouse event
        function setPosition(e: MouseEvent) {
            var rect = canvas.getBoundingClientRect();
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
    }, [])

    return (
        <div className="whiteboard-container">
            <canvas id="whiteboard-canvas" width="1000" height="600"></canvas>
        </div>
    );
}
