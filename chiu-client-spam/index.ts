import { client as WebSocket, connection, IMessage } from "websocket";
import EventSource from "eventsource";

const address: string = process.env.API_URL || "localhost:8080";

// SENDING DATA
const socket = new WebSocket();
socket.on("connect", (ws: connection) => {
    ws.on("close", (code: number, description: string) => {
        console.log("Disconnected from /report: ", code, description);
    });

    ws.on("error", (error: Error) => {
        console.error("Error from /report: ", error.message);
    });

    ws.addListener("message", (data: IMessage) => {
        console.log("Message from /report: ", data.utf8Data);
    });

    setInterval(() => ws.sendUTF(JSON.stringify({
        level: Math.floor(Math.random() * 100)
    })), 100);
});
socket.connect(`ws://${address}/report`);

// RECEIVING DATA
const sse = new EventSource(`http://${address}/listen`);
sse.addEventListener("close", (event: any) => {
    console.log("Disconnected from /listen: ", event);
});

sse.addEventListener("error", (event: any) => {
    console.log("Error from /listen: ", event);
})

sse.addEventListener("noise", (event: any) => {
    console.log("Message from /listen: ", event.data);
});
