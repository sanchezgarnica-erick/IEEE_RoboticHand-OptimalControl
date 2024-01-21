#include "mqttFunctions.h"

//#define MQTT_FUNCTIONS_DEBUG

extern struct SaGaFinger indexFinger;
extern struct SaGaFinger middleFinger;
extern struct SaGaFinger ringFinger;
extern struct SaGaFinger littleFinger;
extern struct SaGaFinger thumb;

extern PID indexFingerPID;
extern PID middleFingerPID;
extern PID ringFingerPID;
extern PID littleFingerPID;
extern PID thumbPID;

const char *rootTopicSubscribe = "<MQTT Root topic suscribe>";
const char *rootTopicPublish = "<MQTT Root topic publish>";
const char *mqttUsername = "MQTT Username";
const char *mqttPassword = "MQTT Password";

AsyncMqttClient mqttClient;
TimerHandle_t mqttReconnectTimer;
TimerHandle_t wifiReconnectTimer;

void connectToWifi()
{
    #ifdef MQTT_FUNCTIONS_DEBUG
        Serial.println("Connecting to Wi-Fi...");
    #endif
    WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
}

void connectToMqtt()
{
    #ifdef MQTT_FUNCTIONS_DEBUG
        Serial.println("mqttCoreId: " + String(xPortGetCoreID()));
        Serial.print("Connecting to MQTT...");
    #endif
    mqttClient.setCredentials(mqttUsername, mqttPassword);
    mqttClient.connect();
    #ifdef MQTT_FUNCTIONS_DEBUG
        Serial.println("Connected!");
    #endif
}

void WiFiEvent(WiFiEvent_t event)
{
    #ifdef MQTT_FUNCTIONS_DEBUG
        Serial.printf("[WiFi-event] event: %d\n", event);
    #endif
    switch (event)
    {
    case SYSTEM_EVENT_STA_GOT_IP:
        #ifdef MQTT_FUNCTIONS_DEBUG
            Serial.println("WiFi connected");
            Serial.println("IP address: ");
            Serial.println(WiFi.localIP());
        #endif
        connectToMqtt();
        break;
    case SYSTEM_EVENT_STA_DISCONNECTED:
        #ifdef MQTT_FUNCTIONS_DEBUG
            Serial.println("WiFi lost connection");
        #endif
        xTimerStop(mqttReconnectTimer, 0); // ensure we don't reconnect to MQTT while reconnecting to Wi-Fi
        xTimerStart(wifiReconnectTimer, 0);
        break;
    default:
        break;
    }
}

void onMqttConnect(bool sessionPresent)
{
    #ifdef MQTT_FUNCTIONS_DEBUG
        Serial.println("Connected to MQTT.");
        Serial.print("Session present: ");
        Serial.println(sessionPresent);
    #endif
    uint16_t packetIdSub = mqttClient.subscribe(rootTopicSubscribe, 0);
    #ifdef MQTT_FUNCTIONS_DEBUG
        Serial.print("Subscribing at QoS 2, packetId: ");
        Serial.println(packetIdSub);
    #endif
    mqttClient.publish(rootTopicPublish, 0, false, "SaGaHand Connected!");
    #ifdef MQTT_FUNCTIONS_DEBUG
        Serial.println("Publishing at QoS 0");
    #endif
}

void onMqttDisconnect(AsyncMqttClientDisconnectReason reason)
{
    #ifdef MQTT_FUNCTIONS_DEBUG
        Serial.println("Disconnected from MQTT." + String((int) reason));
    #endif
    if (WiFi.isConnected())
    {
        xTimerStart(mqttReconnectTimer, 0);
    }
}

void onMqttSubscribe(uint16_t packetId, uint8_t qos) 
{
    #ifdef MQTT_FUNCTIONS_DEBUG
        Serial.println("Subscribe acknowledged.");
        Serial.print("  packetId: ");
        Serial.println(packetId);
        Serial.print("  qos: ");
        Serial.println(qos);
    #endif
}

void onMqttUnsubscribe(uint16_t packetId)
{
    #ifdef MQTT_FUNCTIONS_DEBUG
        Serial.println("Unsubscribe acknowledged.");
        Serial.print("  packetId: ");
        Serial.println(packetId);
    #endif
}

void onMqttMessage(char *topic, char *payload, AsyncMqttClientMessageProperties properties, size_t len, size_t index, size_t total)
{
    char code;
    String message = "";
    int colonPos;
    double value[12];
    int i;
    for (int i = 0; i < len; i++)
    {
        message += (char)payload[i];
    }
    message.trim();
    
    code = message.charAt(0);
    if(code == 'a') { //Status Request
        message = "Ok";
        mqttClient.publish(rootTopicPublish, 0, false, message.c_str());
    } 
    if(code == 'b') { //General values
        message = "info:{\"indexFingerSetPoint\":";
        message += String(indexFinger.setPoint);
        message += ", \"indexFingerControl\":";
        message += String(indexFinger.destination);
        message += ", \"indexFingerState\":";
        message += String(indexFinger.state);
        message += ", \"middleFingerSetPoint\":";
        message += String(middleFinger.setPoint);
        message += ", \"middleFingerControl\":";
        message += String(middleFinger.destination);
        message += ", \"middleFingerState\":";
        message += String(middleFinger.state);
        message += ", \"ringFingerSetPoint\":";
        message += String(ringFinger.setPoint);
        message += ", \"ringFingerControl\":";
        message += String(ringFinger.destination);
        message += ", \"ringFingerState\":";
        message += String(ringFinger.state);
        message += ", \"littleFingerSetPoint\":";
        message += String(littleFinger.setPoint);
        message += ", \"littleFingerControl\":";
        message += String(littleFinger.destination);
        message += ", \"littleFingerState\":";
        message += String(littleFinger.state);
        message += ", \"thumbSetPoint\":";
        message += String(thumb.setPoint);
        message += ", \"thumbControl\":";
        message += String(thumb.destination);
        message += ", \"thumbState\":";
        message += String(thumb.state);
        message += "}";
        #ifdef MQTT_FUNCTIONS_DEBUG
            Serial.println("Mensaje para enviar: " + message);
        #endif
        mqttClient.publish(rootTopicPublish, 0, false, message.c_str());
    } 
    if(code == 'c') { //PID values
        message = "PID:{\"indexFingerKp\":";
        message += String(indexFinger.Kp);
        message += ", \"indexFingerKi\":";
        message += String(indexFinger.Ki);
        message += ", \"indexFingerKd\":";
        message += String(indexFinger.Kd);
        message += ", \"middleFingerKp\":";
        message += String(middleFinger.Kp);
        message += ", \"middleFingerKi\":";
        message += String(middleFinger.Ki);
        message += ", \"middleFingerKd\":";
        message += String(middleFinger.Kd);
        message += ", \"ringFingerKp\":";
        message += String(ringFinger.Kp);
        message += ", \"ringFingerKi\":";
        message += String(ringFinger.Ki);
        message += ", \"ringFingerKd\":";
        message += String(ringFinger.Kd);
        message += ", \"littleFingerKp\":";
        message += String(littleFinger.Kp);
        message += ", \"littleFingerKi\":";
        message += String(littleFinger.Ki);
        message += ", \"littleFingerKd\":";
        message += String(littleFinger.Kd);
        message += ", \"thumbKp\":";
        message += String(thumb.Kp);
        message += ", \"thumbKi\":";
        message += String(thumb.Ki);
        message += ", \"thumbKd\":";
        message += String(thumb.Kd);
        message += "}";
        #ifdef MQTT_FUNCTIONS_DEBUG
            Serial.println("Mensaje para enviar: " + message);
        #endif
        mqttClient.publish(rootTopicPublish, 0, false, message.c_str());
    } 
    if(code == 'g') { //Grasping mode ON
        middleFinger.graspMode = 1;
    }
    if(code == 'h') { //Grasping mode OFF
        middleFinger.graspMode = 0;
    } 
    if (code == 's') //Set Point
    {
        i = 0;
        colonPos = message.indexOf(":");
        while (colonPos != -1)
        {
            value[i++] = message.substring(colonPos + 1).toDouble();
            #ifdef MQTT_FUNCTIONS_DEBUG
                Serial.println("Value: " + String(value[i]));
            #endif
            colonPos = message.indexOf(":", colonPos + 1);
        }
        #ifdef MQTT_FUNCTIONS_DEBUG
            Serial.println("Publish received.");
            Serial.println("testFingerSetPoint: " + String(value[i]));
        #endif
        indexFinger.setPoint = value[0];
        middleFinger.setPoint = value[1];
        ringFinger.setPoint = value[2];
        littleFinger.setPoint = value[3];
        thumb.setPoint = value[4];
        indexFinger.setPointReach = 0;
        middleFinger.setPointReach = 0;
        ringFinger.setPointReach = 0;
        littleFinger.setPointReach = 0;
        thumb.setPointReach = 0;
    }
    if (code == 'p') //Set PID Tunning
    {
        i = 0;
        colonPos = message.indexOf(":");
        while (colonPos != -1)
        {
            value[i++] = message.substring(colonPos + 1).toDouble();
            #ifdef MQTT_FUNCTIONS_DEBUG
                Serial.println("Value: " + String(value[i]));
            #endif
            colonPos = message.indexOf(":", colonPos + 1);
        }
        #ifdef MQTT_FUNCTIONS_DEBUG
            Serial.println("Publish received.");
            Serial.println("testFingerSetPoint: " + String(value[i]));
        #endif
        indexFinger.Kp = value[0];
        indexFinger.Ki = value[1];
        indexFinger.Kd = value[2];
        middleFinger.Kp = value[3];
        middleFinger.Ki = value[4];
        middleFinger.Kd = value[5];
        ringFinger.Kp = value[6];
        ringFinger.Ki = value[7];
        ringFinger.Kd = value[8];
        littleFinger.Kp = value[9];
        littleFinger.Ki = value[10];
        littleFinger.Kd = value[11];
        thumb.Kp = value[9];
        thumb.Ki = value[10];
        thumb.Kd = value[11];

        indexFingerPID.SetTunings(indexFinger.Kp, indexFinger.Ki, indexFinger.Kd);
        middleFingerPID.SetTunings(middleFinger.Kp, middleFinger.Ki, middleFinger.Kd);
        ringFingerPID.SetTunings(ringFinger.Kp, ringFinger.Ki, ringFinger.Kd);
        littleFingerPID.SetTunings(littleFinger.Kp, littleFinger.Ki, littleFinger.Kd);
        thumbPID.SetTunings(thumb.Kp, thumb.Ki, thumb.Kd);
    }
}

void onMqttPublish(uint16_t packetId)
{
    #ifdef MQTT_FUNCTIONS_DEBUG
        Serial.println("Publish acknowledged.");
        Serial.print("  packetId: ");
        Serial.println(packetId);
    #endif
}

void mqttSetup()
{
    #ifdef MQTT_FUNCTIONS_DEBUG
        Serial.println();
        Serial.println();
    #endif

    mqttReconnectTimer = xTimerCreate("mqttTimer", pdMS_TO_TICKS(2000), pdFALSE, (void *)0, reinterpret_cast<TimerCallbackFunction_t>(connectToMqtt));
    wifiReconnectTimer = xTimerCreate("wifiTimer", pdMS_TO_TICKS(2000), pdFALSE, (void *)0, reinterpret_cast<TimerCallbackFunction_t>(connectToWifi));

    WiFi.onEvent(WiFiEvent);

    mqttClient.onConnect(onMqttConnect);
    mqttClient.onDisconnect(onMqttDisconnect);
    mqttClient.onSubscribe(onMqttSubscribe);
    mqttClient.onUnsubscribe(onMqttUnsubscribe);
    mqttClient.onMessage(onMqttMessage);
    mqttClient.onPublish(onMqttPublish);
    mqttClient.setServer(MQTT_HOST, MQTT_PORT);

    connectToWifi();
}