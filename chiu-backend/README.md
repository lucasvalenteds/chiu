# Project Chiu: Back-end

## Development

### Application

![open-jdk]

| Description | Command |
| :--- | :--- |
| Run the app | `./gradlew run` |
| Run the tests | `./gradlew test` |
| Package the app | `./gradlew shadowJar` |

### Database

![docker] ![docker-compose]

| Description | Command |
| :--- | :--- |
| Create a new database | `docker-compose up -d` |
| Start the database | `docker-compose start` |
| Stop the database | `docker-compose stop` |
| Enter the database | `docker-compose exec -it chiu-db mongo` |

[open-jdk]: https://img.shields.io/badge/openjdk-11-5382A1.svg?style=for-the-badge "OpenJDK 11"

[docker]: https://img.shields.io/badge/docker-18.09-007BFF.svg?style=for-the-badge "Docker 18.09"

[docker-compose]: https://img.shields.io/badge/docker--compose-1.21-007BFF.svg?style=for-the-badge "Docker Compose 1.21"
