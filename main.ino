#include <Ethernet.h>
#include <ArduinoJson.h>
    
EthernetClient ethernetClient;

void setup()
{
    Serial.begin(9600);

    // Setup internet connection
    byte arduinoIpAddress[] = { 10, 0, 0, 177 };
    byte arduinoMacAddress[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };
    Ethernet.begin(arduinoMacAddress, arduinoIpAddress);
    Serial.println(Ethernet.linkStatus());

    // Create the request payload in JSON
    const size_t bufferSize = JSON_OBJECT_SIZE(1);
    DynamicJsonBuffer jsonBuffer(bufferSize);
    JsonObject &root = jsonBuffer.createObject();
    root["level"] = "120";
    root.printTo(Serial);
}


void loop()
{

}