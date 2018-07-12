; Modulo principal, se encarga de disparar todos los otros modulos
(defmodule MAIN (export ?ALL))

; Modulo utilizado para obtener las preferencias del comensal
(defmodule obtener_preferencias
  (import MAIN ?ALL)
  (export ?ALL)
)

; Modulo utilizado para inferir nuevas preferencias a partir del
; perfil que hemos obtenido del comensal
(defmodule inferir_preferencias
  (import MAIN ?ALL)
  (export ?ALL)
)

; Modulo utilizado para descartar platos si incumplen alguna de las 
; restricciones impuestas por el usuario
(defmodule descartar_platos
  (import MAIN ?ALL)
  (export ?ALL)
)

; Modulo utilizado para puntuar platos en funcion de las preferencias del
; usuario y las inferidas
(defmodule puntuar_platos
  (import MAIN ?ALL)
  (import descartar_platos ?ALL)
  (import inferir_preferencias ?ALL)
  (export ?ALL)
)

; Modulo utilizado para crear todos los posibles menues que se pueden 
; construir con los platos supervivientes
(defmodule crear_menus
  (import MAIN ?ALL)
  (import obtener_preferencias ?ALL)
  (import puntuar_platos ?ALL)
  (export ?ALL)
)

; Modulo utilizado para puntuar los menues creados en su conjunto, en 
; funcion de las sinergias que surgen de la combinacion de sus elementos
(defmodule puntuar_menus
  (import MAIN ?ALL)
  (import crear_menus ?ALL)
  (export ?ALL)
)

; Modulo final donde, una vez puntuados todos los menues, se imprimen
; los tres mejores siguiendo criterios distintos para ello
(defmodule escoger_menus
  (import MAIN ?ALL)
  (import puntuar_menus ?ALL)
  (export ?ALL)
)
