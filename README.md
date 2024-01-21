# Resources for the manuscript "Optimal Control and Grasping for a Robotic Hand with a Non-linked Double Tendon Arrangement"
![Graphic Abstract](https://github.com/sanchezgarnica-erick/IEEE_RoboticHand-OptimalControl/blob/main/ProjectImages/graphicAbstract_v2.png)

El proyecto presentado en el artículo utiliza:
- El brazo derecho del modelo [Inmoov](https://inmoov.fr/), colocando los dedos en una arreglo de doble tendón no ligado.
- Unidades de Medición Inercial para determinar la posición de los dedos.
-  Servomotores como actuadores para los dedos.
- Una interfaz para intercambio de mensajes mediante el protocolo MQTT con la finalidad de recibir comandos de voz desde una aplicación web
- El movimiento de los dedos está condicionado por un controlador PI óptimo.

Para lograr lo anterior se utiliza el microcontrolador ESP32, en el cual se aplicaron técnicas de programación multitarea en paralelo.
La carpeta ESP32 contiene los archivos que deben instalarse en el microcontrolador, en los cuales hay que actualizar la siguiente información:
- *mpu6050Functions.cpp*: 

<!--stackedit_data:
eyJoaXN0b3J5IjpbMTU2ODk5NTk0OCwxMzIzMDkzMjE4LDEyMD
Y5OTA2OTIsNzk3NTYxNjgsLTEzNTUxMjQ4ODNdfQ==
-->