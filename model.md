#Documento de modelo

Este documento explica como funciona el modelo de pedestrian avoidance propuesto.

##Inicialización
Se setean por configuración los siguientes parámetros:

* _externalForceRadiusTreshold_
>Distancia mínima a la que puede estar el future del pedestrian.

* _externalForceTreshold_
>TODO

* _SpringConstant_
>TODO

* _ReactionDistance_
>TODO

* _alpha & beta_
>TODO

* _t_
>TODO


Con estos parámetros y la elección del escenario, se inicializa el programa, se deja a criterio del lector como implementar los generadores y sumideros.

##Ejecución

Cuando empieza la ejecución, en cada instante de tiempo se ejecutan en orden los siguientes componentes:

1. [FutureForceUpdaterComponent](#futureforceupdatercomponent "FutureForceUpdaterComponent")
2. [FuturePositionUpdaterComponent](#futurepositionupdatercomponent "FuturePositionUpdaterComponent")
3. [PedestrianForceUpdaterComponent](#PedestrianForceUpdaterComponent "PedestrianForceUpdaterComponent")
4. [PedestrianPositionUpdaterComponent](#PedestrianPositionUpdaterComponent "PedestrianPositionUpdaterComponent")
5. [GridPedestrianPositionUpdater](#GridPedestrianPositionUpdater "GridPedestrianPositionUpdater")

###FutureForceUpdaterComponent
```bash
por cada peatón:
  fuerzas_sobre_future <- 0
  si el pedestrian esta mas lejos del future que 'radius_treshold':
  	por cada otro_peatón en el rango de visión del peatón actual:
  	  fuerzas_sobre_future <- fuerzas_sobre_future + fuerza_repulsión(peatón, otro_peatón)
  	si fuerzas_sobre_future < externalForceThreshold:
      fuerzas_sobre_future <- 0
      apuntar_future_al_objetivo()
      threshold_activo <- true
    si treshold_activo es false:
      fuerzas_sobre_future <- fuerzas_sobre_future + fuerza_de_deseo(peatón)
    
    fuerzas[future(peaton)] <- fuerzas_sobre_future




fuerza_repulsion(peatón, otro_peatón):
  intensidad <- alpha*e^(-distancia/beta)
  fuerza_repulsion <- normalizar(future_position(peatón) - future_position(otro_peatón)) * intensidad

fuerza_de_deseo(peatón):
  posicion_future_ideal <- encontrar la posición del future si no hubiese ningún tipo de fuerzas sobre él
  v <- posicion_future_ideal - future_position(peatón)
  si distancia(v) >= 0.01
    return normalizar(v) * 50
  si no:
    return 0
```
###FuturePositionUpdaterComponent
```bash
por cada future:
  delta_velocidad <- fuerzas[future] / masa(future) * delta_tiempo //la masa del future es siempre 1
  delta_posición  <- fuerzas[future] / masa(future) * delta_tiempo^2 / 2 + delta_velocidad * delta_tiempo
  delta_velocidad <- delta_velocidad * 0.9
  actualizar(future, delta_posicion, delta_velocidad)
```
###PedestrianForceUpdaterComponent
```bash
por cada peatón:
  fuerzas[peatón] <- 0
  fuerzas[peatón] <- fuerzas[peatón] + fuerza_de_deseo(peatón)
  fuerzas[peatón] <- fuerzas[peatón] + fuerzas_externas(peatón)

fuerza_de_deseo(peatón):
  distancia <- posicion(peatón) - future_position(peatón)
  porcentaje_distancia <- distancia / reactionDistance

  f <- ((future_position(peatón) - posicion(peatón)) * velocidad_maxima(peatón) - velocidad(peatón)) * masa(peatón) / TAO
  return f * porcentaje_distancia

fuerzas_externas(peatón):
  fuerza <- 0
  por cada otro otro_peatón y obstáculo (obstáculo):
    si se están tocando:
      overlapping <- overlapping(peatón, obstáculo)
      fuerza <- fuerza + vector_normal(peatón, obstáculo) * SpringConstant * overlapping
  return fuerza
```
###PedestrianPositionUpdaterComponent
```bash
por cada peatón:
  delta_velocidad <- fuerzas[peatón] / masa(peatón) * delta_tiempo
  delta_posición  <- fuerzas[peatón] / masa(peatón) * delta_tiempo^2 / 2 + delta_velocidad * delta_tiempo
  delta_velocidad <- delta_velocidad * 0.9
  actualizar(peatón, delta_posicion, delta_velocidad)
```
