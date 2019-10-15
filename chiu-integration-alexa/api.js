const EventSource = require("eventsource")

const getNoiseLevel = url => new Promise((resolve, reject) => {
    const sse = new EventSource(url);

    sse.addEventListener("noise", event => {
        resolve(JSON.parse(event.data).level);
        sse.close();
    });

    sse.addEventListener("error", error => {
        reject(error);
    });
});

module.exports = {
    getNoiseLevel,
};
