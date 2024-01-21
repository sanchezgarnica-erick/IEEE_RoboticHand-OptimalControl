#include "mpu6050Functions.h"

//#define MPU6050_FUNCTIONS_DEBUG
//#define OUTPUT_READABLE_MPU6050
//#define OUTPUT_BINARY_MPU6050
//#define OUTPUT_READABLE_ANGLES 

TCA9548 MP(0x70);

extern struct SaGaFinger indexFinger;
extern struct SaGaFinger middleFinger;
extern struct SaGaFinger ringFinger;
extern struct SaGaFinger littleFinger;
extern struct SaGaFinger thumb;

double radToDegFactor = 180.00/PI;
double accelFactor = 16384.00;
double gyroFactor = 131.00;
double interval;
double preInterval = millis();
double gyroCoef = 0.96;
double accCoef = 0.04;

TaskHandle_t mpu6050TaskHandle;

void mpu6050Setup() {
    Wire.begin();
    
    // initialize device
    #ifdef MPU6050_FUNCTIONS_DEBUG
        Serial.println("Initializing I2C devices...");
    #endif
    
    MP.selectChannel(indexFinger.sensorChannel);
    indexFinger.sensor->initialize();
    indexFinger.sensor->resetSensors();

    MP.selectChannel(middleFinger.sensorChannel);
    middleFinger.sensor->initialize();
    middleFinger.sensor->resetSensors();

    MP.selectChannel(ringFinger.sensorChannel);
    ringFinger.sensor->initialize();
    ringFinger.sensor->resetSensors();

    MP.selectChannel(littleFinger.sensorChannel);
    littleFinger.sensor->initialize();
    littleFinger.sensor->resetSensors();

    MP.selectChannel(thumb.sensorChannel);
    thumb.sensor->initialize();
    thumb.sensor->resetSensors();
	
    #ifdef MPU6050_FUNCTIONS_DEBUG
		// verify connection
        Serial.println("Testing device connections...");
        MP.selectChannel(indexFinger.sensorChannel);
        Serial.println(indexFinger.sensor->testConnection() ? "MPU6050 IndexFinger connection successful" : "MPU6050 IndexFinger connection failed");

        MP.selectChannel(middleFinger.sensorChannel);
        Serial.println(middleFinger.sensor->testConnection() ? "MPU6050 MiddleFinger connection successful" : "MPU6050 MiddleFinger connection failed");

        MP.selectChannel(ringFinger.sensorChannel);
        Serial.println(ringFinger.sensor->testConnection() ? "MPU6050 RingFinger connection successful" : "MPU6050 RingFinger connection failed");

        MP.selectChannel(littleFinger.sensorChannel);
        Serial.println(littleFinger.sensor->testConnection() ? "MPU6050 LittleFinger connection successful" : "MPU6050 LittleFinger connection failed");

        MP.selectChannel(thumb.sensorChannel);
        Serial.println(thumb.sensor->testConnection() ? "MPU6050 Thumb connection successful" : "MPU6050 Thumb connection failed");
    #endif
	
	//IMU parameters. They may vary among different devices
    MP.selectChannel(indexFinger.sensorChannel);
    indexFinger.sensor->setXAccelOffset(1512);
    indexFinger.sensor->setYAccelOffset(-5237);
    indexFinger.sensor->setZAccelOffset(934);
    indexFinger.sensor->setXGyroOffset(63);
    indexFinger.sensor->setYGyroOffset(-79);
    indexFinger.sensor->setZGyroOffset(40);

    MP.selectChannel(middleFinger.sensorChannel);
    middleFinger.sensor->setXAccelOffset(-1882);
    middleFinger.sensor->setYAccelOffset(3208);
    middleFinger.sensor->setZAccelOffset(1432);
    middleFinger.sensor->setXGyroOffset(9);
    middleFinger.sensor->setYGyroOffset(-31);
    middleFinger.sensor->setZGyroOffset(41);

    MP.selectChannel(ringFinger.sensorChannel);
    ringFinger.sensor->setXAccelOffset(-6276);
    ringFinger.sensor->setYAccelOffset(-2275);
    ringFinger.sensor->setZAccelOffset(1012);
    ringFinger.sensor->setXGyroOffset(102);
    ringFinger.sensor->setYGyroOffset(-25);
    ringFinger.sensor->setZGyroOffset(-19);

    MP.selectChannel(littleFinger.sensorChannel);
    littleFinger.sensor->setXAccelOffset(-750);
    littleFinger.sensor->setYAccelOffset(979);
    littleFinger.sensor->setZAccelOffset(824);
    littleFinger.sensor->setXGyroOffset(70);
    littleFinger.sensor->setYGyroOffset(67);
    littleFinger.sensor->setZGyroOffset(-41);

    MP.selectChannel(thumb.sensorChannel);
    thumb.sensor->setXAccelOffset(-600);
    thumb.sensor->setYAccelOffset(-2154);
    thumb.sensor->setZAccelOffset(1336);
    thumb.sensor->setXGyroOffset(-187);
    thumb.sensor->setYGyroOffset(-59);
    thumb.sensor->setZGyroOffset(20);

    #ifdef MPU6050_FUNCTIONS_DEBUG
        Serial.println("MPU6050 setup completed");
    #endif
}

void mpu6050Update(struct SaGaFinger *finger) {
    int16_t rawAccX, rawAccY, rawAccZ;
    int xAng, yAng, zAng;
    int minVal = 265;
    int maxVal = 402;
    int x, y, z;

    MP.selectChannel(finger->sensorChannel);

    do {
        rawAccX = finger->sensor->getAccelerationX();
        rawAccY = finger->sensor->getAccelerationY();
        rawAccZ = finger->sensor->getAccelerationZ();
    } while(rawAccX == -1 || rawAccY == -1 || rawAccZ == -1 );

    xAng = map(rawAccX, minVal, maxVal, -90, 90);
    yAng = map(rawAccY, minVal, maxVal, -90, 90);
    zAng = map(rawAccZ, minVal, maxVal, -90, 90);
    
    x = RAD_TO_DEG * (atan2(-yAng, -zAng) + PI);
    y = RAD_TO_DEG * (atan2(-xAng, -zAng) + PI);
    z = RAD_TO_DEG * (atan2(-yAng, -xAng) + PI);

    if (x < 225)
        finger->state = 180 - x;
    else
        finger->state = 180 + (361 - x);
}

void mpu6050Read(void *parameter) {
    #ifdef MPU6050_FUNCTIONS_DEBUG
        Serial.println("mpu6050CoreId: " + String(xPortGetCoreID()));
    #endif
    for(;;) {
        mpu6050Update(&indexFinger);
        mpu6050Update(&middleFinger);
        mpu6050Update(&ringFinger);
        mpu6050Update(&littleFinger);
        mpu6050Update(&thumb);
        middleFinger.force = analogRead(FORCE_SENSOR);
        if(middleFinger.setPointReach == 1 && middleFinger.graspMode == 1) {
            if(middleFinger.force <= 700) {
                indexFinger.setPoint += 0.1;
                middleFinger.setPoint += 0.1;
                ringFinger.setPoint += 0.1;
                littleFinger.setPoint += 0.1;
                thumb.setPoint += 0.1;
                vTaskDelay(5);
            }
            if(indexFinger.setPoint > 250) {
                indexFinger.setPoint = 250;
            }
            if(middleFinger.setPoint > 250) {
                middleFinger.setPoint = 250;
            }
            if(ringFinger.setPoint > 240) {
                middleFinger.setPoint = 240;
            }
            if(littleFinger.setPoint > 240) {
                littleFinger.setPoint = 240;
            }
            if(thumb.setPoint > 160) {
                middleFinger.setPoint = 160;
            }
        }
        vTaskDelay(1);
    }
}

void mpu6050StartTask() {
    xTaskCreatePinnedToCore(
        mpu6050Read,
        "mpu6050ReadTask",
        10000,
        NULL,
        1,
        &mpu6050TaskHandle,
        0
    );
}

void mpu6050StopTask() {
    vTaskDelete(mpu6050TaskHandle);
}

void mpu6050Print() {
    #ifdef OUTPUT_READABLE_MPU6050
        // display tab-separated accel/gyro x/y/z values
        Serial.print("a/g:\t");
        Serial.print(ax); Serial.print("\t");
        Serial.print(ay); Serial.print("\t");
        Serial.print(az); Serial.print("\t");
        Serial.print(gx); Serial.print("\t");
        Serial.print(gy); Serial.print("\t");
        Serial.println(gz);
    #endif

    #ifdef OUTPUT_BINARY_MPU6050
        Serial.write((uint8_t)(ax >> 8)); Serial.write((uint8_t)(ax & 0xFF));
        Serial.write((uint8_t)(ay >> 8)); Serial.write((uint8_t)(ay & 0xFF));
        Serial.write((uint8_t)(az >> 8)); Serial.write((uint8_t)(az & 0xFF));
        Serial.write((uint8_t)(gx >> 8)); Serial.write((uint8_t)(gx & 0xFF));
        Serial.write((uint8_t)(gy >> 8)); Serial.write((uint8_t)(gy & 0xFF));
        Serial.write((uint8_t)(gz >> 8)); Serial.write((uint8_t)(gz & 0xFF));
    #endif

    #ifdef OUTPUT_READABLE_ANGLES
        Serial.print("[X, Y, G, x(t)],");
        int16_t rawAccX, rawAccY, rawAccZ;
        int16_t rawGyroX, rawGyroY, rawGyroZ;
        Serial.print(String((float)rawAccX) + ", " + String((float)rawAccY) + ", " + String((float)rawAccZ) + ", " + String((float)rawGyroX) + ", " + String((float)rawGyroY) + ", " + String((float)rawGyroZ) + ", ");
        Serial.println(String(testFingerSensorAngleX) + ", " + String(testFingerSensorAngleY) + ", " + String(testFingerSensorGravity) + ", " + String(testFingerSensorState) + "," + String(testFingerServoDestination) + "," + String(testFingerSetPoint));
        Serial.println("testFingerSensorState:" + String(testFinger.state) + ", testFingerServoDestination:" + String(testFinger.destination/100) + ", testFingerSetPoint:" + String(testFinger.setPoint));
    #endif
}