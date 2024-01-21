#include <Arduino.h>
#include "utils.h"
#include "servoFunctions.h"
#include "mpu6050Functions.h"
#include "mqttFunctions.h"
#include "pidFunctions.h"
#include <math.h>

void setup()
{
  Serial.begin(115200);
  utilsSetup();
  servoSetup();
  mpu6050Setup();
  pidSetup();
  mqttSetup();
  servoStartTask();
  mpu6050StartTask();
  pidStartTask();
  delay(5000);
  Serial.println("force,setPointReach,graspMode,time,indexX,middleX,ringX,littleX,thumbX,indexSP,middleSP,ringSP,littleSP,thumbSP,indexU,middleU,ringU,littleU,thumbU");
}

extern struct SaGaFinger indexFinger;
extern struct SaGaFinger middleFinger;
extern struct SaGaFinger ringFinger;
extern struct SaGaFinger littleFinger;
extern struct SaGaFinger thumb;
void loop()
{
  Serial.print("upLim:350, indexState:" + String(indexFinger.state) + ", indexControl:" + String(indexFinger.destination/10) + ", indexSetPoint:" + String(indexFinger.setPoint));
  Serial.print(", middleState:" + String(middleFinger.state) + ", middleControl:" + String(middleFinger.destination/10) + ", middleSetPoint:" + String(middleFinger.setPoint));
  Serial.print(", ringState:" + String(ringFinger.state) + ", ringControl:" + String(ringFinger.destination/10) + ", ringFingerSetPoint:" + String(ringFinger.setPoint));
  Serial.print(", littleState:" + String(littleFinger.state) + ", littleControl:" + String(littleFinger.destination/10) + ", littleFingerSetPoint:" + String(littleFinger.setPoint));
  Serial.print(", thumbState:" + String(thumb.state) + ", thumbControl:" + String(thumb.destination/10) + ", thumbSetPoint:" + String(thumb.setPoint) + ", downLim: -50");
  Serial.println(", forceSensor:" + String(analogRead(FORCE_SENSOR)));
  delay(1);
}
 