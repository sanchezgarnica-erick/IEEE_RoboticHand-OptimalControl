#ifndef UTILS_H
#define UTILS_H

#define FORCE_SENSOR 34

#include "MPU6050.h"


struct SaGaFinger{
    //Servo variables
    uint8_t servoChannel;
    double position = 0;
    double destination = 0;
    
    //Sensor variables
    MPU6050 *sensor;
    uint8_t sensorAddr = 0x68;
    uint8_t sensorChannel;
    double angleX = 0;
    double angleY = 0;
    double gravity = 0;
    double state = 0;
    
    //PID values
    double setPoint = 0;
    double Kp;
    double Ki;
    double Kd;

    //Grasp mode variables
    int graspMode = 0;
    int force = 0;
    int setPointReach = 0;
};

void utilsSetup();

#endif