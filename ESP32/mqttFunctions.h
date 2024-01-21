#ifndef mqttFunctions_H
#define mqttFunctions_H

#include <WiFi.h>
extern "C" {
	#include "freertos/FreeRTOS.h"
	#include "freertos/timers.h" 
}
#include <AsyncMqttClient.h>
#include "utils.h"
#include "PID_v1.h"

#define WIFI_SSID "<WiFi Name>"
#define WIFI_PASSWORD "<WiFi Password>"

#define MQTT_HOST "<MQTT Host name>"
#define MQTT_PORT <MQTT Port>

void connectToWifi();
void connectToMqtt(); 
void WiFiEvent(WiFiEvent_t event);
void onMqttConnect(bool sessionPresent);
void onMqttDisconnect(AsyncMqttClientDisconnectReason reason);
void onMqttSubscribe(uint16_t packetId, uint8_t qos);
void onMqttUnsubscribe(uint16_t packetId);
void onMqttMessage(char* topic, char* payload, AsyncMqttClientMessageProperties properties, size_t len, size_t index, size_t total);
void onMqttPublish(uint16_t packetId);
void mqttSetup();

#endif