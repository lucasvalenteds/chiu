import { RequestHandler, HandlerInput, SkillBuilders, getIntentName } from "ask-sdk-core";
import { Response } from "ask-sdk-model";
import * as ChiuAPI from "./api";

const CurrentNoiseLevelHandler: RequestHandler = {
    canHandle(handlerInput: HandlerInput): boolean {
        return getIntentName(handlerInput.requestEnvelope) === process.env.AWS_INTENT_NAME;
    },
    async handle(handlerInput: HandlerInput): Promise<Response> {
        try {
            const noiseLevel: string = await ChiuAPI.getNoiseLevel(process.env.CHIU_API_URL);

            return handlerInput.responseBuilder
                .speak("O nível de ruído atual é " + noiseLevel + ".")
                .getResponse();
        } catch (error) {
            return handlerInput.responseBuilder
                .speak("Não foi possível obter o nível de ruído atual.")
                .getResponse();
        }
    },
};

exports.handler = SkillBuilders
    .custom()
    .addRequestHandlers(CurrentNoiseLevelHandler)
    .lambda();