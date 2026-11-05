# Proyecto de drones

Este proyecto sirve para gestionar drones y pilotos. Lo he hecho para el examen de DSA.

## Qué hace

- Se pueden añadir drones y pilotos.
- Se pueden ver los drones y los pilotos ordenados por horas de vuelo.
- Se puede guardar un dron en el almacén para repararlo.
- Se puede reparar el dron que está más cerca de la puerta del almacén.
- Se puede crear un plan de vuelo para un dron y un piloto.
- Se pueden ver los planes de vuelo de un piloto o de un dron.

## Cómo está hecho

- Los drones y los pilotos se guardan en un HashMap, usando su id.
- El almacén es una pila: el último dron que entra es el primero que sale.
- Hay una clase DronManagerImpl que hace todo el trabajo y es un Singleton.
- Los mensajes del programa se escriben con LOG4J.
- Hay tests con JUnit para comprobar que funciona.

## Cómo probarlo

Ejecutar los tests con: mvn clean test

## Estado actual

- Hecho: el interfaz, la clase DronManagerImpl y los tests.
- Falta: el servicio REST.
- 
## Pendiente

- Hacer el servicio REST.