#include <ArduinoHttpClient.h>
#include <b64.h>
#include <HttpClient.h>
#include <WebSocketClient.h>
#include <Ethernet.h>
#include <ArduinoJson.h>

// Setup internet connection
EthernetClient ethernetClient;

// Setup server connection
char serverIp[] = "192.168.1.8";
int serverPort = 8080;
WebSocketClient client = WebSocketClient(ethernetClient, serverIp, serverPort);

// Setup JSON object that will be sent to server
const size_t bufferSize = JSON_OBJECT_SIZE(1);
DynamicJsonBuffer jsonBuffer(bufferSize);
JsonObject &payloadJson = jsonBuffer.createObject();

void setup()
{
    // Setup Arduino connection
    Serial.begin(9600);

    // Setup internet connection
    byte arduinoIpAddress[] = {192, 168, 1, 177};
    byte arduinoMacAddress[] = {0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED};
    Ethernet.begin(arduinoMacAddress, arduinoIpAddress);
}

void loop()
{
    client.begin();

    while (client.connected())
    {
        // Serialize the current noise level as JSON
        payloadJson["level"].set(random(120));

        // Convert the JSON object to a String object
        String payload;
        payloadJson.printTo(payload);

        // Send the String object to the server
        client.beginMessage(TYPE_TEXT);
        client.print(payload);
        client.endMessage();

        // Wait some time before sending data to server again
        delay(500);
    }
}