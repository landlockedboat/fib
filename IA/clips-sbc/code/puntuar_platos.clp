; Clase que guarda un plato, su puntuacion y las justificaciones de esta
(defclass puntuar_platos::PlatoPonderado 
	(is-a USER)
	(role concrete)
	(slot plato
		(type INSTANCE)
		(create-accessor read-write))
	(slot puntuacion
		(type INTEGER)
		(create-accessor read-write))
	(multislot justificaciones
		(type STRING)
		(create-accessor read-write))
)

; Clase que guarda una bebida, su puntuacion y las justificaciones de esta
(defclass puntuar_platos::BebidaPonderada
	(is-a USER)
	(role concrete)
	(slot bebida
		(type INSTANCE)
		(create-accessor read-write))
	(slot puntuacion
		(type INTEGER)
		(create-accessor read-write))
	(multislot justificaciones
		(type STRING)
		(create-accessor read-write))
)

(deffunction puntuar_platos::puntuar_cualidad 
  "Funcion para puntuar un plato en funcion de una cualidad suya"
  (?platoP ?cond ?mens ?cant)

  (bind ?plato (send ?platoP get-plato))
  (bind ?punt (send ?platoP get-puntuacion))
  (bind $?just (send ?platoP get-justificaciones))
  (bind ?nombrePlato (send ?plato get-nombre_plato))

  (if (eq ?cond TRUE) then
    (bind ?nuevaPunt (+ ?punt ?cant)) 
    (bind ?text (format nil "%s es %s: +%d puntos" ?nombrePlato ?mens ?cant))
  else
    (bind ?nuevaPunt (- ?punt ?cant)) 
    (bind ?text (format nil "%s es %s: -%d puntos" ?nombrePlato ?mens ?cant))
  ) 
  ; Informacion para debugar
  ; (printout t ?text crlf)
  
  (bind $?just (insert$ $?just (+ (length$ $?just) 1) ?text))
  (send ?platoP put-justificaciones $?just)
  (send ?platoP put-puntuacion ?nuevaPunt)
)

(defrule puntuar_platos::crear_instancias_ponderadas
  "Crea todas las instancias de platoPonderado y bebidaPonderada"
  (declare (salience 10))
  (not (puntuacionesCreadas))
  ?l <- (listaPlatos $?listaPlatos)
  ?b <- (listaBebidas $?listaBebidas)
  =>
  (progn$ (?plato $?listaPlatos)
      (make-instance (gensym) of PlatoPonderado 
      (plato ?plato) 
      (puntuacion 0)
    )
  )

  (progn$ (?bebida $?listaBebidas)
      (make-instance (gensym) of BebidaPonderada 
      (bebida ?bebida) 
      (puntuacion 0)
    )
  )

  (assert (puntuacionesCreadas))
)

(defrule puntuar_platos::puntuar_platos_temporada
  "Puntua positiva o negativamente un plato en funcion de si al comensal
   le gustan los platos de temporada o no"
	?platoP <- (object (is-a PlatoPonderado) 
              (plato ?plato)
              (puntuacion ?punt)
              (justificaciones $?just)
            )
  (platos_temporada ?cond)
  (not (puntuar_platos_temporada ?plato))
  (datos_comensal (epoca_ano ?epoca))
  =>
  (bind $?ingredientes (send ?plato get-compuesto_por))
  (bind ?encontrado FALSE)
  (progn$ (?ing $?ingredientes)
    (bind $?disponibilidades (send ?ing get-disponibilidad))
    (progn$ (?disp $?disponibilidades)
      (if
        (= (str-compare ?disp ?epoca) 0)
      then
        (puntuar_cualidad ?platoP ?cond "de temporada" 10)
        (bind ?encontrado TRUE)
        (break)
      )
    )
    (if (eq ?encontrado TRUE) then (break))
  )
  (assert (puntuar_platos_temporada ?plato))
)

(defrule puntuar_platos::puntuar_platos_exclusivos
  "Regla que puntua positiva o negativamente un plato en funcion de si 
  al comensal le gustan los platos exclusivos o no"
	?platoP <- (object (is-a PlatoPonderado) 
              (plato ?plato)
              (puntuacion ?punt)
              (justificaciones $?just)
            )
  (platos_exclusivos ?cond)
  (not (refinar_platos_exclusivos ?plato))
  =>
  (bind ?exclusivo (send ?plato get-exclusivo))
  (bind ?nombrePlato (send ?plato get-nombre_plato))
    (if
      (eq ?exclusivo TRUE)
    then
      (puntuar_cualidad ?platoP ?cond "exclusivo" 10)
  )
  (assert (refinar_platos_exclusivos ?plato))
)

(defrule puntuar_platos::puntuar_platos_modernos
  "Regla que puntua positiva o negativamente un plato en funcion de si 
  al comensal le gustan los platos modernos o no"
	?platoP <- (object (is-a PlatoPonderado) 
              (plato ?plato)
              (puntuacion ?punt)
              (justificaciones $?just)
            )
  (platos_modernos ?cond)
  (not (refinar_platos_modernos ?plato))
  =>
  (bind ?moderno (send ?plato get-moderno))
  (bind ?nombrePlato (send ?plato get-nombre_plato))
  (if
    (eq ?moderno TRUE)
  then
    (puntuar_cualidad ?platoP ?cond "moderno" 10)
  )
  (assert (refinar_platos_modernos ?plato))
)


(defrule puntuar_platos::puntuar_platos_tradicionales
  "Regla que puntua positiva o negativamente un plato en funcion de si 
  al comensal le gustan los platos tradicionales o no"
	?platoP <- (object (is-a PlatoPonderado) 
              (plato ?plato)
              (puntuacion ?punt)
              (justificaciones $?just)
            )
  (platos_tradicionales ?cond)
  (not (refinar_platos_tradicionales ?plato))
  =>
  (bind ?tradicional (send ?plato get-tradicional))
  (bind ?nombrePlato (send ?plato get-nombre_plato))
  (if
    (eq ?tradicional TRUE)
  then
    (puntuar_cualidad ?platoP ?cond "tradicional" 10)
  )
  (assert (refinar_platos_tradicionales ?plato))
)

(defrule puntuar_platos::puntuar_platos_pesados
  "Regla que puntua positiva o negativamente un plato en funcion de si 
  al comensal le gustan los platos pesados o no"
	?platoP <- (object (is-a PlatoPonderado) 
              (plato ?plato)
              (puntuacion ?punt)
              (justificaciones $?just)
            )
  (platos_pesados ?cond)
  (not (refinar_platos_pesados ?plato))
  =>
  (bind ?pesado (send ?plato get-pesado))
  (bind ?nombrePlato (send ?plato get-nombre_plato))
    (if
      (eq ?pesado TRUE)
    then
      (puntuar_cualidad ?platoP ?cond "pesado" 10)
  )
  (assert (refinar_platos_pesados ?plato))
)

(defrule puntuar_platos::puntuar_platos_espanoles
  "Regla que puntua positiva o negativamente un plato en funcion de si 
  al comensal le gustan los platos del pais o no"
	?platoP <- (object (is-a PlatoPonderado) 
              (plato ?plato)
              (puntuacion ?punt)
              (justificaciones $?just)
            )
  (platos_espanoles ?cond)
  (not (refinar_platos_espanoles ?plato))
  =>
  (bind $?tipicoDe (send ?plato get-tipico_de))
  (bind ?nombrePlato (send ?plato get-nombre_plato))

  (bind ?encontrado FALSE)
  (progn$ (?pais $?tipicoDe)
    (bind ?nombrePais (instance-name ?pais))
    (if
      (= (str-compare ?nombrePais Espana) 0)
    then
      (bind ?encontrado TRUE)
    )
  )
  
  (if (eq ?encontrado TRUE) then
    (puntuar_cualidad ?platoP ?cond "tipico de espana" 10)
  )
  (assert (refinar_platos_espanoles ?plato))
)

(defrule puntuar_platos::puntuar_platos_extranjeros
  "Regla que puntua positiva o negativamente un plato en funcion de si 
  al comensal le gustan los platos extranjeros o no"
	?platoP <- (object (is-a PlatoPonderado) 
              (plato ?plato)
              (puntuacion ?punt)
              (justificaciones $?just)
            )
  (platos_extranjeros ?cond)
  (not (refinar_platos_extranjeros ?plato))
  =>
  (bind $?tipicoDe (send ?plato get-tipico_de))
  (bind ?nombrePlato (send ?plato get-nombre_plato))

  (bind ?encontrado FALSE)
  (progn$ (?pais $?tipicoDe)
    (bind ?nombrePais (instance-name ?pais))
    (if
      (= (str-compare ?nombrePais Espana) 0)
    then
      (bind ?encontrado TRUE)
    )
  )
  
  (if (eq ?encontrado FALSE) then
    (puntuar_cualidad ?platoP ?cond "tipico de un pais extranjero" 10)
  )
  (assert (refinar_platos_extranjeros ?plato))
)

(defrule puntuar_platos::puntuar_platos_pequenos
  "Regla que puntua positiva o negativamente un plato en funcion de si 
  al comensal le gustan los platos servidos en raciones pequenas o no"
	?platoP <- (object (is-a PlatoPonderado) 
              (plato ?plato)
              (puntuacion ?punt)
              (justificaciones $?just)
            )
  (platos_pequenos ?cond)
  (not (refinar_platos_pequenos ?plato))
  =>
  (bind ?racion (send ?plato get-racion))
  (bind ?nombrePlato (send ?plato get-nombre_plato))
    (if
      (eq ?racion S)
    then
      (puntuar_cualidad ?platoP ?cond "de racion pequena" 10)
  )
  (assert (refinar_platos_pequenos ?plato))
)

(defrule puntuar_platos::puntuar_platos_grandes
  "Regla que puntua positiva o negativamente un plato en funcion de si 
  al comensal le gustan los platos servidos en raciones grandes o no"
	?platoP <- (object (is-a PlatoPonderado) 
              (plato ?plato)
              (puntuacion ?punt)
              (justificaciones $?just)
            )
  (platos_grandes ?cond)
  (not (refinar_platos_grandes ?plato))
  =>
  (bind ?racion (send ?plato get-racion))
  (bind ?nombrePlato (send ?plato get-nombre_plato))
    (if
      (eq ?racion L)
    then
      (puntuar_cualidad ?platoP ?cond "de racion grande" 10)
  )
  (assert (refinar_platos_grandes ?plato))
)

(defrule puntuar_platos::puntuar_platos_calientes
  "Regla que puntua positiva o negativamente un plato en funcion de si 
  al comensal le gustan los platos servidos calientes o no"
	?platoP <- (object (is-a PlatoPonderado) 
              (plato ?plato)
              (puntuacion ?punt)
              (justificaciones $?just)
            )
  (platos_calientes ?cond)
  (not (refinar_platos_calientes ?plato))
  =>
  (bind ?caliente (send ?plato get-caliente))
  (bind ?nombrePlato (send ?plato get-nombre_plato))
    (if
      (eq ?caliente TRUE)
    then
      (puntuar_cualidad ?platoP ?cond "servido caliente" 10)
  )
  (assert (refinar_platos_calientes ?plato))
)

; Regla usada para debugar
;(defrule puntuar_platos::imprimir_puntuaciones
;  "Regla auxiliar para imprimir las puntuaciones de cada plato"
;  (declare (salience -9))
;	?platoP <- (object (is-a PlatoPonderado) 
;              (plato ?plato)
;              (puntuacion ?punt)
;              (justificaciones $?just)
;            )
;  =>
;  (bind ?nombrePlato (send ?plato get-nombre_plato))
;  ; Informacion para debugar
;  (printout t (format nil "%s: %d" ?nombrePlato ?punt) crlf )
;)

(defrule puntuar_platos::siguiente_modulo
  "Pasa al siguiente modulo una vez todas las reglas se han ejecutado"
  (declare (salience -10))
  =>
  (focus crear_menus)
)
