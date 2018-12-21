import dotenv from "dotenv";
import EventSource from "eventsource";
import { client as WebSocket, connection, IMessage } from "websocket";

if (process.env.NODE_ENV === "prod") {
    dotenv.load();
}

const serverUrlToReport: string = process.env.WS_URL || "ws://localhost:8080/";
const serverUrlToListen: string = process.env.API_URL || "http://localhost:8080/listen";

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
        level: Math.floor(Math.random() * 100),
    })), 250);
});
socket.connect(serverUrlToReport);

// RECEIVING DATA
const sse = new EventSource(serverUrlToListen);
sse.addEventListener("close", (event: any) => {
    console.log("Disconnected from /listen: ", event);
});

sse.addEventListener("error", (event: any) => {
    console.log("Error from /listen: ", event);
})

sse.addEventListener("noise", (event: any) => {
    console.log("Message from /listen: ", event.data);
});
