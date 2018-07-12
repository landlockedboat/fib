(deffunction puntuar_menus::puntuar_textura_platos
  "Funcion auxiliar que se encarga de puntuar platos del menu segun si 
  comparten textura"
  (?platoP1 ?platoP2 ?menu)

  (bind ?plato1 (send ?platoP1 get-plato))
  (bind ?plato2 (send ?platoP2 get-plato))

  (bind ?textura_plato1 (send ?plato1 get-textura))
  (bind ?textura_plato2 (send ?plato2 get-textura))

  (bind ?nombre_plato1 (send ?plato1 get-nombre_plato))
  (bind ?nombre_plato2 (send ?plato2 get-nombre_plato))

  (bind ?punt (send ?menu get-puntuacion))
  (bind $?just (send ?menu get-justificaciones))

  (if (eq ?textura_plato1 ?textura_plato2) then

    (bind ?nuevaPunt (- ?punt 50)) 

    (bind ?text (format nil "%s y %s tienen la misma textura: -50" 
      ?nombre_plato1 ?nombre_plato2))

    (bind $?just (insert$ $?just (+ (length$ $?just) 1) ?text))

    (send ?menu put-justificaciones $?just)
    (send ?menu put-puntuacion ?nuevaPunt)
  )
)

(deffunction puntuar_menus::puntuar_combinaciones_elementos
  "Funcion auxiliar que se encarga de puntuar elementos del menu segun si 
  combinan bien entre ellos"
  (?nombre1 ?nombre2 ?menu ?compatibles)

  (bind ?punt (send ?menu get-puntuacion))
  (bind $?just (send ?menu get-justificaciones))

  (progn$ (?comp ?compatibles)
    (bind ?nombre_comp (send ?comp get-nombre_plato))

    (if (eq ?nombre_comp ?nombre2) then

      (bind ?nuevaPunt (+ ?punt 15)) 

      (bind ?text (format nil "%s y %s van bien juntos: +15" 
        ?nombre1 ?nombre2))

      (bind $?just (insert$ $?just (+ (length$ $?just) 1) ?text))
      (send ?menu put-puntuacion ?nuevaPunt)
      (send ?menu put-justificaciones $?just)
      (break)
    )
  )
)

(defrule puntuar_menus::puntuar_textura
  "Regla que puntua los menues segun si la textura de los platos 
  se repite o no"
  ?menu <- (object (is-a Menu)
            (primer_plato ?primeroP) 
            (bebida_primer_plato ?bebidaP_primero)
            (segundo_plato ?segundoP) 
            (bebida_segundo_plato ?bebidaP_segundo)
            (postre ?postreP) 
            (bebida_postre ?bebidaP_postre)
            (puntuacion ?punt)
            (precio ?precio)
            (justificaciones $?just)
          )
  (not (textura_puntuada ?menu))
  =>
  (puntuar_textura_platos ?primeroP ?segundoP ?menu)
  (puntuar_textura_platos ?segundoP ?postreP ?menu)
  (assert (textura_puntuada ?menu))
)

(defrule puntuar_menus::puntuar_combinaciones
  "Regla que puntua los menues segun si la combinacion de sus ingredientes 
  es satisfactoria"
  ?menu <- (object (is-a Menu)
            (primer_plato ?primeroP) 
            (bebida_primer_plato ?bebidaP_primero)
            (segundo_plato ?segundoP) 
            (bebida_segundo_plato ?bebidaP_segundo)
            (postre ?postreP) 
            (bebida_postre ?bebidaP_postre)
            (puntuacion ?punt)
            (precio ?precio)
            (justificaciones $?just)
          )
  (not (combinaciones_puntuadas ?menu))
  =>

  (bind ?primero (send ?primeroP get-plato))
  (bind ?segundo (send ?segundoP get-plato))
  (bind ?postre (send ?postreP get-plato))

  (bind $?compatibles_primero (send ?primero get-plato_plato))
  (bind $?compatibles_segundo (send ?segundo get-plato_plato))
  (bind $?compatibles_postre (send ?postre get-plato_plato))

  (bind ?nombre_primero (send ?primero get-nombre_plato))
  (bind ?nombre_segundo (send ?segundo get-nombre_plato))
  (bind ?nombre_postre (send ?postre get-nombre_plato))

  (bind ?nuevaPunt ?punt)
  (puntuar_combinaciones_elementos ?nombre_primero ?nombre_segundo 
    ?menu $?compatibles_primero)

  (puntuar_combinaciones_elementos ?nombre_segundo ?nombre_postre 
    ?menu $?compatibles_segundo)

  (puntuar_combinaciones_elementos ?nombre_primero ?nombre_postre 
    ?menu $?compatibles_primero)

  (bind ?bebida_primero (send ?bebidaP_primero get-bebida))
  (bind ?bebida_segundo (send ?bebidaP_segundo get-bebida))
  (bind ?bebida_postre (send ?bebidaP_postre get-bebida))

  (bind ?nombre_bebida_primero (send ?bebida_primero get-nombre_bebida))
  (bind ?nombre_bebida_segundo (send ?bebida_segundo get-nombre_bebida))
  (bind ?nombre_bebida_postre (send ?bebida_postre get-nombre_bebida))

  (if (eq (type ?bebida_primero) Vino) then
    (bind $?compatibles (send ?bebida_primero get-vino_plato))

    (puntuar_combinaciones_elementos ?nombre_bebida_primero ?nombre_primero 
      ?menu $?compatibles)
  )

  (if (eq (type ?bebida_segundo) Vino) then
    (bind $?compatibles (send ?bebida_segundo get-vino_plato))

    (puntuar_combinaciones_elementos ?nombre_bebida_segundo ?nombre_segundo 
      ?menu $?compatibles)
  )

  (if (eq (type ?bebida_postre) Vino) then
    (bind $?compatibles (send ?bebida_postre get-vino_plato))

    (puntuar_combinaciones_elementos ?nombre_bebida_postre ?nombre_postre 
      ?menu $?compatibles)
  )

  (assert (combinaciones_puntuadas ?menu))
)

(defrule puntuar_menus::siguiente_modulo
  "Pasa al siguiente modulo una vez todas las reglas se han ejecutado"
  (declare (salience -10))
  =>
  (focus escoger_menus)
)
