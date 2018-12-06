# Project Chiu: Back-end

## Development

### Application

| Description | Command |
| :--- | :--- |
| Run the app | `./gradlew run` |
| Run the tests | `./gradlew test` |
| Package the app | `./gradlew shadowJar` |
| Deploy to production | `./gradlew deployHeroku` |

### Database

| Description | Command |
| :--- | :--- |
| Create a new database | `docker-compose up -d` |
| Start the database | `docker-compose start` |
| Stop the database | `docker-compose stop` |
| Enter the database | `docker-compose exec -it chiu-db mongo` |