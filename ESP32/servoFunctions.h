#ifndef SERVO_FUNCTIONS_H
#define SERVO_FUNCTIONS_H

#include "Adafruit_PWMServoDriver.h"
#include "utils.h"

#define SERVO_MIN 700
#define SERVO_MAX 2200
#define SERVO_FREQ 50
#define SERVO_INPUT_MIN 0
#define SERVO_INPUT_MAX 1500

void servoSetup();
void servoStartTask();
void servoStopTask();
int saturator(double value);
void initServo();
void moveServo(void *parameter);

#endif