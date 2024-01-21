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
		+ *b*: Envío de los valores actuales de las variables.
		+ *c*: Envío de las ganancias actuales del PID.
		+ *g*: Activar modo de agarre.
		+ *h*: Desactivar modo de agarre
		+ *s*: Recibir el setpoint para cada uno de los dedos.
		+ *p*: Recibir las nuevas ganancias para el controlar PID de cada uno de los dedos.
- *mqttFunctions.h* [Lines 13-17]: Nombre y Contraseña del WiFi. Nombre y puerto del servidor MQTT
- *utils.cpp*, función utilsSetup(): Configuración de las ganancias para el PID de cada uno de los dedos.
- *utils.h* [Lines 4]: Número de pin del sensor de fuerza.

<!--stackedit_data:
eyJoaXN0b3J5IjpbLTMzMTU1NzA1NywxNTQ1Mjg0NDEzLDEzMj
MwOTMyMTgsMTIwNjk5MDY5Miw3OTc1NjE2OCwtMTM1NTEyNDg4
M119
-->