# Project Chiu

Chiu provides a way to monitor the noise level on a given location.

This is an IoT experiment made to help to prevent hearing disability due to noise pollution. The data collected may be useful to identity how the noise pollution has been increasing in some places.

It uses an [Arduino](https://arduino.cc) to capture the noise level in decibels (dB) and send it in real-time to a webserver via [Web Sockets](https://developer.mozilla.org/en-US/docs/Web/API/WebSockets_API). The data sent to the server can be monitored in a website in real-time or using the API based on [Server-Sent Events](https://developer.mozilla.org/en-US/docs/Web/API/Server-sent_events).

The back-end is written in Java and based on Reactor Netty, the data is persisted on MongoDB and the front-end is written in TypeScript and is based on Vue.

## Preview

| Noise level meter | Reference values |
| :---: | :---: |
| ![Website showing noise level in real-time using a chart](./docs/preview-meter.gif) | ![Table with common noise levels for comparison](./docs/preview-level.png) |

## How to run

### Back-end

![jdk] ![docker] ![compose]

| Description | Command |
| :--- | :--- |
| Run service | `./gradlew run` |
| Run unit tests | `./gradlew test` |
| Run integration test | `./gradlew itest` |
| Run producer for test | `./gradlew runConsumer` |
| Run consumer for test | `./gradlew runProducer` |
| Run stress tests | `./gradlew gatlingRun` |

[jdk]: https://img.shields.io/badge/jdk-1.8-5481A0.svg?style=for-the-badge "JDK 1.8"
[docker]: https://img.shields.io/badge/docker-19.03-007BFF.svg?style=for-the-badge "Docker 19.03"
[compose]: https://img.shields.io/badge/docker--compose-1.2-039BC6.svg?style=for-the-badge "Docker Compose 1.2"

### Front-end

![node] ![npm]

| Description | Command |
| :--- | :--- |
| Install dependencies | `npm install` |
| Run unit tests | `npm run test:unit` |
| Run app for development | `npm run serve` |
| Build app for production | `npm run build` |

[node]: https://img.shields.io/badge/node-12-73B657.svg?style=for-the-badge "Node 12"
[npm]: https://img.shields.io/badge/npm-6-C93B39.svg?style=for-the-badge "NPM 6"

### Hardware

| Description | Command |
| :--- | :--- |
| Arduino support for Visual Studio Code | `ext install vscode-arduino` |

| Quantity | Item | Reference |
| :---: | :--- | :--- |
| 1 | Arduino board | Arduino Uno |
| 1 | Ethernet Shield | HansRun w5100 |
| 1 | Sound meter sensor | KY-038 |

### Infrastructure

| Description | Command |
| :--- | :--- |
| Provision the database | `docker-compose --file mongo.yml up --detach` |
