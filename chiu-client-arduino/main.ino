#include <ArduinoHttpClient.h>
#include <b64.h>
#include <HttpClient.h>
#include <WebSocketClient.h>
#include <Ethernet.h>
#include <ArduinoJson.h>

EthernetClient ethernetClient;

char serverIp[] = "chiu-app.herokuapp.com";
WebSocketClient client = WebSocketClient(ethernetClient, serverIp);

const size_t bufferSize = JSON_OBJECT_SIZE(1);
DynamicJsonBuffer jsonBuffer(bufferSize);
JsonObject &payloadJson = jsonBuffer.createObject();

void setup()
{
    Serial.begin(9600);

    byte arduinoIpAddress[] = {192, 168, 1, 177};
    byte arduinoMacAddress[] = {0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED};
    Ethernet.begin(arduinoMacAddress, arduinoIpAddress);
}

void loop()
{
    client.begin();

    while (client.connected())
    {
        payloadJson["level"].set(random(120));

        String payload;
        payloadJson.printTo(payload);

        client.beginMessage(TYPE_TEXT);
        client.print(payload);
        client.endMessage();

        delay(500);
    }
}