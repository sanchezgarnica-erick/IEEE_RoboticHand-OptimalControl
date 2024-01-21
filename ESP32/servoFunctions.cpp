#include "servoFunctions.h"

//#define SERVO_FUNCTIONS_DEBUG

#define SERVO_SDA_PIN 33
#define SERVO_SCL_PIN 32

extern struct SaGaFinger indexFinger;
extern struct SaGaFinger middleFinger;
extern struct SaGaFinger ringFinger;
extern struct SaGaFinger littleFinger;
extern struct SaGaFinger thumb;

TwoWire busI2C = TwoWire(1);
Adafruit_PWMServoDriver pwm = Adafruit_PWMServoDriver(0x40, busI2C);

TaskHandle_t servoTaskHandle;

void servoSetup() {
    busI2C.begin(SERVO_SDA_PIN, SERVO_SCL_PIN);
    pwm.begin();
    pwm.setOscillatorFrequency(27000000);
    pwm.setPWMFreq(SERVO_FREQ);
    delay(3000);
    initServo();
}

int saturator(double value)
{
  value += SERVO_MIN;
  if (value < SERVO_MIN)
    value = SERVO_MIN;
  else if (value > SERVO_MAX)
    value = SERVO_MAX;
  return (int)value;
}

void initServo() {
    pwm.writeMicroseconds(indexFinger.servoChannel, saturator(0));
    delay(2000);
    pwm.writeMicroseconds(middleFinger.servoChannel, saturator(0));
    delay(2000);
    pwm.writeMicroseconds(ringFinger.servoChannel, saturator(0));
    delay(2000);
    pwm.writeMicroseconds(littleFinger.servoChannel, saturator(0));
    delay(2000);
    pwm.writeMicroseconds(thumb.servoChannel, saturator(0));
    delay(2000);
}

void moveServoGen(struct SaGaFinger *finger) {
    if(finger->position != finger->destination) {
        if(finger->position < finger->destination) {
            finger->position+=2; 
        } else {
            finger->position-=2;
        }
    }
}

void moveServo(void *parameter) {
    #ifdef SERVO_FUNCTIONS_DEBUG
        Serial.println("servoCoreId: " + String(xPortGetCoreID()));
    #endif
    for(;;) {
        moveServoGen(&indexFinger);
        pwm.writeMicroseconds(indexFinger.servoChannel, saturator(indexFinger.position));
        moveServoGen(&middleFinger);
        pwm.writeMicroseconds(middleFinger.servoChannel, saturator(middleFinger.position));
        moveServoGen(&ringFinger);
        pwm.writeMicroseconds(ringFinger.servoChannel, saturator(ringFinger.position));
        moveServoGen(&littleFinger);
        pwm.writeMicroseconds(littleFinger.servoChannel, saturator(littleFinger.position));
        moveServoGen(&thumb);
        pwm.writeMicroseconds(thumb.servoChannel, saturator(thumb.position));
        vTaskDelay(2);
    }
}

void servoStartTask() {
    xTaskCreatePinnedToCore(
        moveServo,
        "moveServoTask",
        10000,
        NULL,
        1,
        &servoTaskHandle,
        0
    );
}

void servoStopTask() {
    vTaskDelete(servoTaskHandle);
}