# Contributing

## Feedback

Please [open an issue](https://github.com/lucasvalenteds/chiu/issues/new) to share your opinion, report bugs and discuss features.

## Versioning

All changes in the code must be made in a separated Git branch other than `master` branch. Writing meaningful commit messages are very recommended and the text must contains the context where the change applies followed by `:`, like the examples below.

* `backend: Changed response status code for /listen`
* `frontend: Fixes about button click event`
* `root: Fixes typo in README file`

## Folders

| Folder | Description |
| :--- | :--- |
| `chiu-backend` | Server side code to handle persistence and provide data to clients |
| `chiu-frontend` | Website to consume data from the back-end |
| `chiu-client-arduino` | Code to be uploaded to the device |
| `chiu-client-spam` | App to simulate the device for testing |

## Files

| File | Context | Description |
| :--- | :--- | :--- |
| `application.conf` | Back-end | Settings for development and production |
| `.env.production` | Front-end | Settings for production |
| `.env.development` | Front-end | Settings for development |
| `.env` | Spam | Settings for production and development |

## Development

Each folder have its own `README.md` file with usage instructions. Please refer to them to see all available commands.

## Deployment

### Heroku

1. Go to your [account page](https://dashboard.heroku.com/account) and get the API key 
2. Run the command `HEROKU_API_KEY=<your_key> ./deploy.sh`
