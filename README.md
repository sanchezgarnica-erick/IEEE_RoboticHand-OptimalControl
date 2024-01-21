# Resources for the manuscript "Optimal Control and Grasping for a Robotic Hand with a Non-linked Double Tendon Arrangement"
![Graphic Abstract](https://github.com/sanchezgarnica-erick/IEEE_RoboticHand-OptimalControl/blob/main/ProjectImages/graphicAbstract_v2.png)

El proyecto presentado en el artículo utiliza:
- El brazo derecho del modelo [Inmoov](https://inmoov.fr/), colocando los dedos en una arreglo de doble tendón no ligado.
- Unidades de Medición Inercial para determinar la posición de los dedos.
-  Servomotores como actuadores para los dedos.
- Una interfaz para intercambio de mensajes mediante el protocolo MQTT con la finalidad de recibir comandos de voz desde una aplicación web
- El movimiento de los dedos está condicionado por un controlador PI óptimo.

Para lograr lo anterior se utiliza el microcontrolador ESP32, en el cual se aplicaron técnicas de programación multitarea en paralelo.



<!--stackedit_data:
eyJoaXN0b3J5IjpbLTEzNzczODkzNjAsMTMyMzA5MzIxOCwxMj
A2OTkwNjkyLDc5NzU2MTY4LC0xMzU1MTI0ODgzXX0=
-->