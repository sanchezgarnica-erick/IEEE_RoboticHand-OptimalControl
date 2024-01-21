# Resources for the manuscript "Optimal Control and Grasping for a Robotic Hand with a Non-linked Double Tendon Arrangement"
![Graphic Abstract](https://github.com/sanchezgarnica-erick/IEEE_RoboticHand-OptimalControl/blob/main/ProjectImages/graphicAbstract_v2.png)

The project presented in the article uses:
- The right arm of the [Inmoov](https://inmoov.fr/), configuring the fingers in a non-linked double tendon arrangement.
- Inertial Measurement Units to determine the finger positions.
-  Servo motors as actuators for the fingers.
- An interface for message exchange using the MQTT protocol to receive voice commands from a web application.
- Finger movements are controlled by an optimal PI controller.

To achieve the above, the ESP32 microcontroller is used, applying parallel multitasking programming techniques. The ESP32 folder contains the files that need to be installed on the microcontroller, where the following information needs to be updated:

- *mpu6050Functions.cpp* [Lines 73-112]: Offsets of the IMUs for each finger.
- *mqttFunctions.cpp* [Lines 17-20]: Subscriber and publisher topics, as well as username and password for the MQTT server.
	* The onMqttMessage* function [Line 123]: This section defines the codes for the commands that the prototype can execute.
		+ *a*: Sending the current system status.
		+ *b*: Sending the current values of the variables.
		+ *c*: Sending the current PID gains.
		+ *g*: Activating the grip mode.
		+ *h*: Deactivating the grip mode.
		+ *s*: Receiving the setpoint for each finger.
		+ *p*: Receiving the new gains for the PID controller of each finger.
- *mqttFunctions.h* [Lines 13-17]: WiFi name and password. MQTT server name and port.
- *utils.cpp*, utilsSetup() function: Configuration of the gains for the PID of each finger.
- *utils.h* [Lines 4]: Pin number of the force sensor.

La aplicación web desarrollada para este artículo, mediante la cual se pueden enviar comandos de voz a la mano, a través del protocolo MQTT, está desarrollada en Java utilizando el Framework Spring. La carpeta *WebApp* contiene los archivos necesario para importar la aplicación como un proyecto Spring/Maven en el STS IDE.
<!--stackedit_data:
eyJoaXN0b3J5IjpbLTE2MzU3MDgxNDksMjc2NzQ0MzQsMTY4OT
k1Mjg1NCwxNTQ1Mjg0NDEzLDEzMjMwOTMyMTgsMTIwNjk5MDY5
Miw3OTc1NjE2OCwtMTM1NTEyNDg4M119
-->