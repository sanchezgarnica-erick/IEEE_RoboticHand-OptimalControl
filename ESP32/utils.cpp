#include "utils.h"

struct SaGaFinger indexFinger;
struct SaGaFinger middleFinger;
struct SaGaFinger ringFinger;
struct SaGaFinger littleFinger;
struct SaGaFinger thumb;


void utilsSetup() {
    indexFinger.sensor = new MPU6050(indexFinger.sensorAddr);
    indexFinger.servoChannel = 0;
    indexFinger.sensorChannel = 0;
    indexFinger.Kp = 3.3672;
    indexFinger.Ki = 1.6729;
    indexFinger.Kd = 0;

    middleFinger.sensor = new MPU6050(middleFinger.sensorAddr);
    middleFinger.servoChannel = 1;
    middleFinger.sensorChannel = 1;
    middleFinger.Kp = 4.4969;
    middleFinger.Ki = 1.9782;
    middleFinger.Kd = 0;

    ringFinger.sensor = new MPU6050(ringFinger.sensorAddr);
    ringFinger.servoChannel = 2;
    ringFinger.sensorChannel = 2;
    ringFinger.Kp = 5.7577;
    ringFinger.Ki = 2.6175;
    ringFinger.Kd = 0;

    littleFinger.sensor = new MPU6050(littleFinger.sensorAddr);
    littleFinger.servoChannel = 3;
    littleFinger.sensorChannel = 3;
    littleFinger.Kp = 5.3826;
    littleFinger.Ki = 2.2642;
    littleFinger.Kd = 0;

    thumb.sensor = new MPU6050(thumb.sensorAddr);
    thumb.servoChannel = 4;
    thumb.sensorChannel = 4;
    thumb.Kp = 7.5321;
    thumb.Ki = 3.2483;
    thumb.Kd = 0;

}
