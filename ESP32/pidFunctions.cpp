#include "pidFunctions.h"

//#define PID_FUNCTIONS_DEBUG

extern struct SaGaFinger indexFinger;
extern struct SaGaFinger middleFinger;
extern struct SaGaFinger ringFinger;
extern struct SaGaFinger littleFinger;
extern struct SaGaFinger thumb;

//Index Finger Variables
double ifState = 0;
PID indexFingerPID(&ifState, &indexFinger.destination, &indexFinger.setPoint, indexFinger.Kp, indexFinger.Ki, indexFinger.Kd, DIRECT);

//Middle Finger Variables
double mfState = 0;
PID middleFingerPID(&mfState, &middleFinger.destination, &middleFinger.setPoint, middleFinger.Kp, middleFinger.Ki, middleFinger.Kd, DIRECT);

//Ring Finger Variables
double rfState = 0;
PID ringFingerPID(&rfState, &ringFinger.destination, &ringFinger.setPoint, ringFinger.Kp, ringFinger.Ki, ringFinger.Kd, DIRECT);

//Little Finger Variables
double lfState = 0;
PID littleFingerPID(&lfState, &littleFinger.destination, &littleFinger.setPoint, littleFinger.Kp, littleFinger.Ki, littleFinger.Kd, DIRECT);

//Thumb Variables
double tState = 0;
PID thumbPID(&tState, &thumb.destination, &thumb.setPoint, thumb.Kp, thumb.Ki, thumb.Kd, DIRECT);

TaskHandle_t pidTaskHandle;

void pidSetup() {
    indexFingerPID.SetTunings(indexFinger.Kp, indexFinger.Ki, indexFinger.Kd);
    indexFingerPID.SetSampleTime(10);
    indexFingerPID.SetMode(AUTOMATIC);
    indexFingerPID.SetOutputLimits(0, 1500);

    middleFingerPID.SetTunings(middleFinger.Kp, middleFinger.Ki, middleFinger.Kd);
    middleFingerPID.SetSampleTime(10);
    middleFingerPID.SetMode(AUTOMATIC);
    middleFingerPID.SetOutputLimits(0, 1500);

    ringFingerPID.SetTunings(ringFinger.Kp, ringFinger.Ki, ringFinger.Kd);
    ringFingerPID.SetSampleTime(10);
    ringFingerPID.SetMode(AUTOMATIC);
    ringFingerPID.SetOutputLimits(0, 1500);

    littleFingerPID.SetTunings(littleFinger.Kp, littleFinger.Ki, littleFinger.Kd);
    littleFingerPID.SetSampleTime(10);
    littleFingerPID.SetMode(AUTOMATIC);
    littleFingerPID.SetOutputLimits(0, 1500);

    thumbPID.SetTunings(thumb.Kp, thumb.Ki, thumb.Kd);
    thumbPID.SetSampleTime(10);
    thumbPID.SetMode(AUTOMATIC);
    thumbPID.SetOutputLimits(0, 1500);
    
    #ifdef PID_FUNCTIONS_DEBUG
        Serial.println("PID setup completed");
    #endif
}

void pidComputeGen(struct SaGaFinger *finger, double *fingerState) {
    if(finger->state < finger->setPoint+3 && finger->state > finger->setPoint-3) {
        *fingerState = finger->setPoint;
        finger->setPointReach = 1;
    } else {
        *fingerState = finger->state;
    }
}

void pidCompute(void *parameter) {
    for(;;) {
        pidComputeGen(&indexFinger, &ifState);
        indexFingerPID.Compute();
        
        pidComputeGen(&middleFinger, &mfState);
        middleFingerPID.Compute();

        pidComputeGen(&ringFinger, &rfState);
        ringFingerPID.Compute();

        pidComputeGen(&littleFinger, &lfState);
        littleFingerPID.Compute();

        pidComputeGen(&thumb, &tState);
        thumbPID.Compute();
        vTaskDelay(1);
    }
    vTaskDelay(1);
}

void pidStartTask() {
    xTaskCreatePinnedToCore(
        pidCompute,
        "pidComputeTask",
        10000,
        NULL,
        1,
        &pidTaskHandle,
        1
    );
}

void pidStopTask() {
    vTaskDelete(pidTaskHandle);
}