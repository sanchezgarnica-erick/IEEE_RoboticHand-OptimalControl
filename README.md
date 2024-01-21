# Resources for the manuscript "Optimal Control and Grasping for a Robotic Hand with a Non-linked Double Tendon Arrangement"
![Graphic Abstract](https://github.com/sanchezgarnica-erick/IEEE_RoboticHand-OptimalControl/blob/main/ProjectImages/graphicAbstract_v2.png)

The project presented in the article uses:
- The right arm of the [Inmoov](https://inmoov.fr/), configuring the fingers in a non-linked double tendon arrangement.
- Inertial Measurement Units to determine the finger positions.
-  Servo motors as actuators for the fingers.
- An interface for message exchange using the MQTT protocol to receive voice commands from a web application.
- Finger movements are controlled by an optimal PI controller.

To achieve the above, the ESP32 microcontroller is used, applying parallel multitasking programming techniques. The ESP32 folder contains the files that need to be installed on the microcontroller, where the following information needs to be updated:

- *mpu6050Functions.cpp* [lines 73-112]: Offsets de las IMUs por cada dedo.
- *mqttFunctions.cpp* [lines 17.20]: Tópicos suscriptor y publicador, así como usuario y password para el servidor MQTT.
	* La Función onMqttMessage* [Línea 123]: Aquí se encuentran definidos los códigos de los comandos que puede ejecutar el prototipo. 
		+ *a*: Envío del estatus actual del sistema.
		+ *b*: Envío de los valores actuales de las variables.
		+ *c*: Envío de las ganancias actuales del PID.
		+ *g*: Activar modo de agarre.
		+ *h*: Desactivar modo de agarre
		+ *s*: Recibir el setpoint para cada uno de los dedos.
		+ *p*: Recibir las nuevas ganancias para el controlar PID de cada uno de los dedos.
- *mqttFunctions.h* [Línea 13-17]: Nombre y Contraseña del WiFi. Nombre y puerto del servidor MQTT
- *utils.cpp*, función utilsSetup(): Configuración de las ganancias para el PID de cada uno de los dedos.
- *utils.h* [Línea 4]: Número de pin del sensor de fuerza.

<!--stackedit_data:
eyJoaXN0b3J5IjpbLTE2NjA5NjcwMzksMTU0NTI4NDQxMywxMz
IzMDkzMjE4LDEyMDY5OTA2OTIsNzk3NTYxNjgsLTEzNTUxMjQ4
ODNdfQ==
-->