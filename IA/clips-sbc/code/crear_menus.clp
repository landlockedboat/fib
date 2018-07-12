; Clase que utilizamos para guardar toda la informacion referente a un menu
(defclass crear_menus::Menu
	(is-a USER)
	(role concrete)
	(slot primer_plato
		(type INSTANCE)
		(create-accessor read-write))
	(slot bebida_primer_plato
		(type INSTANCE)
		(create-accessor read-write))

	(slot segundo_plato
		(type INSTANCE)
		(create-accessor read-write))
	(slot bebida_segundo_plato
		(type INSTANCE)
		(create-accessor read-write))

	(slot postre
		(type INSTANCE)
		(create-accessor read-write))
	(slot bebida_postre
		(type INSTANCE)
		(create-accessor read-write))

	(slot puntuacion
		(type INTEGER)
		(create-accessor read-write))
	(slot precio
		(type INTEGER)
		(create-accessor read-write))
	(multislot justificaciones
		(type STRING)
		(create-accessor read-write))
)

; Defglobal para mantener un registro de la cantidad de platos, bebidas
; y menus que se han creado durante la ejecucion de este modulo. Tan solo
; sirve para obtener estadisticas utiles, no es vital para el 
; funcionamiento del programa
(defglobal crear_menus
  ?*num_primeros* = 0
  ?*num_segundos* = 0
  ?*num_postres* = 0
  ?*num_bebidas* = 0
  ?*num_menus* = 0
  ?*num_menus_descartados* = 0
)

(deffunction crear_menus::inicializar_menu
  "Funcion auxiliar para crear una instancia de un menu"
  (?pp1 ?pp2 ?pp3 ?bebidaP1 ?bebidaP2 ?bebidaP3 ?pMin ?pMax)

  (bind ?p1 (send ?pp1 get-plato))
  (bind ?p2 (send ?pp2 get-plato))
  (bind ?p3 (send ?pp3 get-plato))

  (bind ?punt1 (send ?pp1 get-puntuacion))
  (bind ?punt2 (send ?pp2 get-puntuacion))
  (bind ?punt3 (send ?pp3 get-puntuacion))

  (bind ?precio1 (send ?p1 get-precio))
  (bind ?precio2 (send ?p2 get-precio))
  (bind ?precio3 (send ?p3 get-precio))


  (bind ?bebida1 (send ?bebidaP1 get-bebida))
  (bind ?bebida2 (send ?bebidaP2 get-bebida))
  (bind ?bebida3 (send ?bebidaP3 get-bebida))

  (bind ?puntBebida1 (send ?bebidaP1 get-puntuacion))
  (bind ?puntBebida2 (send ?bebidaP2 get-puntuacion))
  (bind ?puntBebida3 (send ?bebidaP3 get-puntuacion))

  (bind ?precioBebida1 (send ?bebida1 get-precio))
  (bind ?precioBebida2 (send ?bebida2 get-precio))
  (bind ?precioBebida3 (send ?bebida3 get-precio))

  (bind ?puntMenu (+ ?punt1 ?punt2 ?punt3 
                      ?puntBebida1 ?puntBebida2 ?puntBebida3))
  (bind ?precioMenu (+ ?precio1 ?precio2 ?precio3 
                        ?precioBebida1 ?precioBebida2 ?precioBebida3))

  ; Condicion para que los menues no repitan platos
  (bind ?np1 (send ?p1 get-nombre_plato))
  (bind ?np2 (send ?p2 get-nombre_plato))
  (bind ?np3 (send ?p3 get-nombre_plato))
  
  (if
    (and
      (neq ?np1 ?np2)
      (neq ?np1 ?np3)
      (neq ?np2 ?np3)
      ; Condicion para que los menues no se salgan
      ; del precio minimo y maximo del usuario
      (<= ?precioMenu ?pMax)
      (>= ?precioMenu ?pMin)
    )
    then
      (make-instance (gensym) of Menu 
        (primer_plato ?pp1) 
	      (bebida_primer_plato ?bebidaP1)
        (segundo_plato ?pp2) 
	      (bebida_segundo_plato ?bebidaP2)
        (postre ?pp3) 
	      (bebida_postre ?bebidaP3)
        (puntuacion ?puntMenu)
        (precio ?precioMenu)
      )
    else
      (bind ?*num_menus_descartados* (+ ?*num_menus_descartados* 1))
  )
)

(defrule crear_menus::crear_platos_ordenados
  "Regla para separar los platos en primeros, segundos y postres"
	?platoP <- (object (is-a PlatoPonderado) 
              (plato ?plato)
            )
  =>
  (bind $?tiposPlato (send ?plato get-tipo))
  (progn$ (?tipo $?tiposPlato)
    (if
      (eq ?tipo Primero)
    then
      (assert (primero ?platoP))
      (bind ?*num_primeros* (+ ?*num_primeros* 1))
      (break)
    )
  )

  (progn$ (?tipo $?tiposPlato)
    (if
      (eq ?tipo Segundo)
    then
      (assert (segundo ?platoP))
      (bind ?*num_segundos* (+ ?*num_segundos* 1))
      (break)
    )
  )

  (progn$ (?tipo $?tiposPlato)
    (if
      (eq ?tipo Postre)
    then
      (assert (postre ?platoP))
      (bind ?*num_postres* (+ ?*num_postres* 1))
      (break)
    )
  )
)

(defrule crear_menus::crear_bebidas
  "Regla para adaptar las bebidas a hechos"
	?bebidaP <- (object (is-a BebidaPonderada))
  =>
  (bind ?*num_bebidas* (+ ?*num_bebidas* 1))
  (assert (bebida ?bebidaP))
)

(defrule crear_menus::inicializar_menus_bebida_unica
  "Regla para crear los menues con bebida igual para todo el menu"
  (primero ?pp1)
  (segundo ?pp2)
  (postre ?pp3)
  (bebida ?bebidaP)
  (datos_comensal (precio_minimo ?pMin) (precio_maximo ?pMax))
  (datos_comensal (quiere_vino_para_cada_plato ?quiereVino))
  (test (or (eq ?quiereVino nil) (eq ?quiereVino FALSE)))
  =>
  (inicializar_menu ?pp1 ?pp2 ?pp3 ?bebidaP ?bebidaP ?bebidaP ?pMin ?pMax)
  (bind ?*num_menus* (+ ?*num_menus* 1))
)

(defrule crear_menus::inicializar_menus_bebida_para_cada_plato
  "Regla para crear los menues con bebida distinta para cada plato"
  (primero ?pp1)
  (segundo ?pp2)
  (postre ?pp3)
  (bebida ?bebidaP1)
  (bebida ?bebidaP2)
  (bebida ?bebidaP3)
  (datos_comensal (precio_minimo ?pMin) (precio_maximo ?pMax))
  (datos_comensal (quiere_vino_para_cada_plato TRUE))
  =>
  (inicializar_menu ?pp1 ?pp2 ?pp3 ?bebidaP1 ?bebidaP2 ?bebidaP3 ?pMin ?pMax)
  (bind ?*num_menus* (+ ?*num_menus* 1))
)

(defrule crear_menus::imprimir_informacion
  "Regla que imprime informacion estadistica interesante"
  (declare (salience -9))
  (not (info_impresa))
  =>
  (printout t crlf)
  (printout t "---------------------------------" crlf)
  (printout t "Obtenidos:" crlf)
  (printout t (format nil "  > %d primeros platos" ?*num_primeros*) crlf)
  (printout t (format nil "  > %d segundos platos" ?*num_primeros*) crlf)
  (printout t (format nil "  > %d postres" ?*num_postres*) crlf)
  (printout t (format nil "  > %d bebidas" ?*num_bebidas*) crlf)

  (printout t "Numero de menus esperado (sin bebida por plato):" crlf)
  (bind ?numMenus1 (* ?*num_primeros* ?*num_segundos* ?*num_postres* 
    ?*num_bebidas*))
  (printout t (format nil "  > %d" ?numMenus1) crlf)
  (printout t (format nil "  > Tiempo de calculo esperado: %d segundos" 
    (/ (* 15 ?numMenus1) 100000)) crlf)

  (printout t "Numero de menus esperado (con bebida por plato):" crlf)
  (bind ?numMenus2 (* ?*num_primeros* ?*num_segundos* ?*num_postres* 
    ?*num_bebidas* ?*num_bebidas* ?*num_bebidas*))
  (printout t (format nil "  > %d" ?numMenus2) crlf)
  (printout t (format nil "  > Tiempo de calculo esperado: %d segundos" 
    (/ (* 15 ?numMenus2) 100000)) crlf)

  (printout t "Numero de menus generados:" crlf)
  (printout t (format nil "  > %d" ?*num_menus*) crlf)
  (printout t "Numero de menus descartados:" crlf)
  (printout t (format nil "  > %d" ?*num_menus_descartados*) crlf)
  (printout t "Numero de menus restantes:" crlf)
  (printout t (format nil "  > %d" 
    (- ?*num_menus* ?*num_menus_descartados*)) crlf)
  (printout t "---------------------------------" crlf)

  (assert (info_impresa))
)

(defrule crear_menus::siguiente_modulo
  "Pasa al siguiente modulo una vez todas las reglas se han ejecutado"
  (declare (salience -10))
  =>
  (focus puntuar_menus)
)
