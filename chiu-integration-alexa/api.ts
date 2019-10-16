const EventSource = require("eventsource");

export function getNoiseLevel(url: string): Promise<string> {
    return new Promise((resolve, reject) => {
        const sse = new EventSource(url);

        sse.addEventListener("noise", event => {
            resolve(JSON.parse(event.data).level);
            sse.close();
        });

        sse.addEventListener("error", error => {
            reject(error);
        });
    });
}