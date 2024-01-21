#ifndef PID_FUNCTIONS_H
#define PID_FUNCTIONS_H

#include <Arduino.h>
#include "PID_v1.h"
#include "utils.h"

void pidSetup();
void pidCompute(void *parameter);
void pidStartTask();
void pidStopTask();

#endif
