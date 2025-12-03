# Investigación Sistema de Cobro de Tarifas

## Características de Arquitectura a Priorizar

- Mantenibilidad
- Testeabilidad
- Escalabilidad
- Confiabilidad

## Contexto

Capsule Corporation está implementando un sistema de cobro de tarifas utilizando tarjetas NFC.  Estas tarjetas permiten el almacenamiento inalámbrico y seguro de información, habilitando transacciones. La implementación involucra tarjetas NFC que interactúan con un Punto de Servicio (POS) en cada autobús. Este POS será desarrollado a través de una aplicación en el lenguaje de programación o tecnología elegida. Eventualmente, estos sistemas POS deben sincronizar información con el sistema central de la compañía para aplicar los cobros a las tarjetas. El cifrado en la tarjeta NFC asegura la confiabilidad y seguridad de los datos de la tarjeta. 

Uno de los desafíos con esta implementación es la falta de conexión a internet en algunos puntos de servicio. Por lo tanto, es necesario asegurar que la información de las tarjetas NFC pueda almacenarse en el POS y sincronizarse con el sistema central cuando el acceso a internet esté disponible. Además, es crucial evitar largos tiempos de espera al abordar un autobús mientras se sincroniza la información.

## Problema a Resolver

Dado este escenario, se busca una solución para el envío asíncrono de información de las tarjetas. Se debe idear un mecanismo o algoritmo para registrar transacciones y eventualmente sincronizarlas con el sistema central. También se debe considerar la posibilidad de errores de comunicación al enviar información, dependiendo de la ubicación del autobús.  Otro factor a considerar es la demanda variable en el sistema central en momentos específicos; por lo tanto, se recomienda la comunicación asíncrona para prevenir la saturación por centralización del sistema.

## Precondiciones

- Generar una aplicación cliente para ser utilizada como POS. Puede ser generada usando cualquier tecnología, para este caso la interfaz de usuario no es relevante.
- El backend no necesita persistir los datos necesariamente, necesita procesar todos los datos en el orden correcto. Es suficiente simular la persistencia de datos.

## 🎯 Objetivos del Proyecto

Este proyecto busca desarrollar una solución robusta para el cobro de tarifas mediante tarjetas NFC que funcione de manera eficiente incluso con conectividad intermitente. La arquitectura debe garantizar:

1. **Operación offline**: Capacidad de registrar transacciones sin conexión a internet
2. **Sincronización asíncrona**: Envío de datos cuando la conectividad esté disponible
3. **Integridad de datos**: Manejo correcto del orden de las transacciones
4. **Tolerancia a fallos**: Recuperación ante errores de comunicación
5.  **Rendimiento óptimo**: Tiempos de respuesta mínimos durante el abordaje

## 🚀 Desafíos Técnicos

- Gestión de cola de transacciones pendientes
- Manejo de reintentos ante fallos de comunicación
- Prevención de duplicación de transacciones
- Balance de carga en el sistema central
- Sincronización ordenada de eventos

---

**Nota**: Este documento describe los requisitos y criterios de evaluación para el sistema de cobro de tarifas mediante NFC de Capsule Corporation. 
