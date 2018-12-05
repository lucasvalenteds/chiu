#include <ArduinoJson.h>

void setup()
{
    Serial.begin(9600);

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