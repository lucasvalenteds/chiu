import { RequestHandler, HandlerInput, SkillBuilders, getIntentName, ErrorHandler, getRequestType } from "ask-sdk-core";
import { LambdaHandler } from "ask-sdk-core/dist/skill/factory/BaseSkillFactory";
import { Response } from "ask-sdk-model";
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

export class CurrentNoiseLevelHandler implements RequestHandler {
    async canHandle(input: HandlerInput): Promise<boolean> {
        const isIntent = getRequestType(input.requestEnvelope) === "IntentRequest";
        const hasExpectedName = getIntentName(input.requestEnvelope) === process.env.AWS_INTENT_NAME;
        const result = isIntent && hasExpectedName;

        console.log(CurrentNoiseLevelHandler.name + "::canHandle::isIntent:", isIntent);
        console.log(CurrentNoiseLevelHandler.name + "::canHandle::hasExpectedName: ", hasExpectedName);
        console.log(CurrentNoiseLevelHandler.name + "::canHandle::result:", result);

        return result;
    }

    async handle(input: HandlerInput): Promise<Response> {
        console.log(CurrentNoiseLevelHandler.name + "::handle: Request envelope:", input.requestEnvelope);

        try {
            const noiseLevel: string = await getNoiseLevel(process.env.CHIU_API_URL);
            console.log(CurrentNoiseLevelHandler.name + "::handle: Noise level retrieved from server:", noiseLevel);

            return input.responseBuilder
                .speak("O nível de ruído atual é " + noiseLevel + ".")
                .getResponse();
        } catch (error) {
            console.error(CurrentNoiseLevelHandler.name + "::handle: Chiu API seems not to be available: " + error.message)
            return input.responseBuilder
                .speak("Não foi possível obter o nível de ruído atual.")
                .getResponse();
        }
    }
};

export class LaunchRequestIntent implements RequestHandler {
    async canHandle(input: HandlerInput): Promise<boolean> {
        const isIntent = getRequestType(input.requestEnvelope) === "LaunchRequest";
        console.log(LaunchRequestIntent.name + "::canHandle::isIntent:", isIntent);

        return
    }

    async handle(input: HandlerInput): Promise<Response> {
        console.log(LaunchRequestIntent.name + "::handle: Request envelope:", input.requestEnvelope);

        return input.responseBuilder
            .speak("Olá do projeto Chiu :)")
            .addDelegateDirective({
                name: process.env.AWS_INTENT_NAME,
                slots: {},
                confirmationStatus: "CONFIRMED",
            })
            .getResponse();
    }
}

export class SessionEndedRequest implements RequestHandler {
    async canHandle(input: HandlerInput): Promise<boolean> {
        const isIntent = getRequestType(input.requestEnvelope) === "SessionEndedRequest";
        console.log(SessionEndedRequest.name + "::canHandle::isIntent:", isIntent);

        return isIntent;
    }

    async handle(input: HandlerInput): Promise<Response> {
        console.log(SessionEndedRequest.name + "::handle: Request envelope:", input.requestEnvelope);

        return input.responseBuilder
            .getResponse();
    }
}

export class GenericErrorHandler implements ErrorHandler {
    async canHandle(input: HandlerInput, error: Error): Promise<boolean> {
        console.log(GenericErrorHandler.name + "::canHandle::input:", input);
        console.log(GenericErrorHandler.name + "::canHandle::error:", error.message);
        return true;
    }

    async handle(input: HandlerInput, error: Error): Promise<Response> {
        console.log(GenericErrorHandler.name + "::handle: Request envelope:", input.requestEnvelope);
        console.log(GenericErrorHandler.name + "::handle::error:", error.message);

        return input.responseBuilder
            .speak("Não entendi. Fale novamente, por favor.")
            .getResponse();
    }
}

export function getLambdaStreamHandler(): LambdaHandler {
    return SkillBuilders.custom()
        .addRequestHandlers(
            new LaunchRequestIntent(),
            new CurrentNoiseLevelHandler(),
            new SessionEndedRequest()
        )
        .addErrorHandlers(
            new GenericErrorHandler()
        )
        .lambda();
}

exports.handler = getLambdaStreamHandler()