const Alexa = require("ask-sdk-core");
const ChiuAPI = require("./api.js");

const CurrentNoiseLevelHandler = {
    canHandle(handlerInput) {
        const type = Alexa.getRequestType(handlerInput.requestEnvelope);
        const name = Alexa.getIntentName(handlerInput.requestEnvelope);

        return (name === "GetCurrentNoiseLevel") && (type === "IntentRequest" || type === "LaunchRequest");
    },
    async handle(handlerInput) {
        const noiseLevel = await ChiuAPI.getNoiseLevel(process.env.API_URL);

        return handlerInput.responseBuilder
            .speak("O nível de ruído atual é " + noiseLevel)
            .getResponse();
    },
};

exports.handler = Alexa.SkillBuilders
    .custom()
    .addRequestHandlers(CurrentNoiseLevelHandler)
    .lambda();
