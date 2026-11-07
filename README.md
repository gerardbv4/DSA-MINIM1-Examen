# Proyecto de drones

Este proyecto sirve para gestionar drones, pilotos y planes de vuelo.

## ¿Qué hace?

- Se pueden añadir drones y pilotos.
- Se pueden ver los drones y los pilotos ordenados por horas de vuelo.
- Se puede guardar un dron en el almacén para repararlo.
- Se puede reparar el dron que está más cerca de la puerta del almacén (formato pila).
- Se puede crear un plan de vuelo para un dron y un piloto.
- Se pueden ver los planes de vuelo de un piloto o de un dron.

## ¿Cómo está hecho?

- Los drones y los pilotos se guardan en un HashMap, usando su id.
- El almacén es una pila: el último dron que entra es el primero que sale.
- Hay una clase DronManagerImpl que hace todo el trabajo y es un Singleton que implementa la interfaz DronManager.
- Los mensajes del programa se escriben con LOG4J.
- Hay tests con JUnit para comprobar que funciona.
- El servicio REST está hecho con Jersey y se puede probar con Swagger.
- El servicio REST usa DTOs para no devolver los objetos del dominio directamente.

## Servicio REST

- GET /drons: lista de drones por horas de vuelo.
- POST /drons: añadir un dron.
- POST /drons/{id}/store: guardar un dron en el almacén.
- POST /drons/repair: reparar el dron más cercano a la puerta.
- GET /drons/{id}/flightplans: planes de un dron.
- GET /pilots: lista de pilotos por horas de vuelo.
- POST /pilots: añadir un piloto.
- GET /pilots/{id}/flightplans: planes de un piloto.
- POST /flightplans: crear un plan de vuelo.

## ¿Cómo probarlo?

- Tests: mvn clean test
- Servicio REST: ejecutar la clase Main y abrir http://localhost:8080/swagger/index.html

## Lo más difícil

Lo más difícil ha sido el plan de vuelo. Había que conseguir que no se solapen los planes de un mismo dron ni de un mismo piloto en las mismas horas. Para hacerlo calculo la hora de fin (hora de inicio + horas de duración) y compruebo que los intervalos no se pisen. Un plan que empieza justo cuando termina otro sí se permite. El procesado de obtejos de fecha y horas y su formato ha sido también un reto con el que familiarizarse.  
##