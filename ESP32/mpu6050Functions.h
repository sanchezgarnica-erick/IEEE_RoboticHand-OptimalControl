#ifndef MPU6050_FUNCTIONS_H
#define MPU6050_FUNCTIONS_H

#include "TCA9548.h"
#include "MPU6050.h"
#include "Wire.h"
#include "math.h" 
#include "utils.h"

void mpu6050Setup();
void mpu6050StartTask();
void mpu6050StopTask();
void mpu6050Read(void *parameter);
void mpu6050Update(struct SaGaFinger *finger);
void mpu6050Print();

#endif
 