(define (domain menus)
  (:requirements [:strips] [:equality] [:typing] [:fluents])

  (:types 
    dia - object
    plato - object
    primero - plato
    segundo - plato

    tipo_plato - object
    )

  (:predicates
    ; NIVEL BASICO
    ; Primer plato p es incompatibe con segundo plato s
    (incompatible ?p - primero ?s - segundo)

    ; EXTENSION 1
    ; Indica que l plato p ya ha sido preparado
    (preparado ?p - plato)

    ; EXTENSION 2
    ; d2 es consecutivo a d1
    (consecutivo ?d1 - dia  ?d2 - dia)
    ; Explicacion de este predicado en problema_1.pddl
    (ultimo_dia ?d - dia)

    ; El plato p es de tipo t
    (es_de_tipo ?p - plato ?t - tipo_plato)

    ; Dia d sirve un primer plato de tipo t
    (tipo_dia_primero ?d - dia ?t - tipo_plato)
    ; Dia d sirve un segundo plato de tipo t
    (tipo_dia_segundo ?d - dia ?t - tipo_plato)

    ; En el dia ?d se ha servido el primer plato
    (servido_primero ?d - dia)
    ; En el dia ?d se ha servido el segundo plato
    (servido_segundo ?d - dia)

    ; El primer plato p ha sido servido en el dia d
    (menu_primero ?d - dia ?p - primero)
    ; El segundo plato s ha sido servido en el dia d
    (menu_segundo ?d - dia ?s - segundo)

    )

  (:functions
    ; EXTENSION 4
    (calorias ?p - plato)
    ; EXTENSION 5
    (precio_total)
    (precio_plato ?p - plato)
    )

  ; Accion para servir el primer plato del ultimo dia.
  ; Es necesario hacerlo de esta manera porque necesitamos
  ; de algun dia que tenga los platos ya servidos
  ; ya que dependemos del tipo de plato del dia
  ; consecutivo al nuestro para servirlo.
  (:action servir_ultimo_dia_primero
           :parameters (
                        ?d - dia 
                        ?p - primero
                        ?tp - tipo_plato
                        ) 
           :precondition (and 
                           (ultimo_dia ?d)
                           (not (servido_primero ?d))
                           (not (preparado ?p))
                           (es_de_tipo ?p ?tp)
                           )
           :effect  (and
                      (servido_primero ?d)
                      (menu_primero ?d ?p)
                      (preparado ?p)
                      (tipo_dia_primero ?d ?tp)
                      )
           )

  ; Lo mismo que servir_ultimo_dia_primero pero esta vez con el segundo
  ; plato
  (:action servir_ultimo_dia_segundo
           :parameters (
                        ?d - dia 
                        ?s - segundo
                        ?ts - tipo_plato 
                        ) 
           :precondition (and 
                           (ultimo_dia ?d)
                           (not (servido_segundo ?d))
                           (not (preparado ?s))
                           (es_de_tipo ?s ?ts)

                           )
           :effect  (and
                      (servido_segundo ?d)
                      (menu_segundo ?d ?s)
                      (preparado ?s)
                      (tipo_dia_segundo ?d ?ts)

                      (increase (precio_total) (precio_plato ?s))
                      )
           )

  ; Esta accion puede ser utilizada sobre el dia d si este no ha servido
  ; todavia su segundo plato. Esto es necesario por la EXTENSION 3, ya
  ; que algunos platos pueden haber sido asignados a dias sin haber 
  ; comprobado su incompatibilidad con el mismo menu o con el dia 
  ; consecutivo.
  (:action servir_primero_solo
           :parameters (
                        ?d - dia 
                        ?p - primero
                        ?t - tipo_plato
                        ?c - dia 
                        ?tc - tipo_plato 
                        ) 
           :precondition (and 
                           (not (servido_primero ?d))
                           (not (servido_segundo ?d))
                           (not (preparado ?p))
                           (es_de_tipo ?p ?t)
                           (consecutivo ?d ?c)
                           (servido_primero ?c)
                           (tipo_dia_primero ?c ?tc)
                           (not (= ?t ?tc))
                           )
           :effect  (and
                      (servido_primero ?d)
                      (preparado ?p)
                      (tipo_dia_primero ?d ?t)
                      (menu_primero ?d ?p)
                      (increase (precio_total) (precio_plato ?p))
                      )
           )

  ; Accion que sirve el primer plato p de un dia d asumiendo
  ; que el segundo plato x no ha sido servido en el dia d.
  (:action servir_primero
           :parameters (
                        ?d - dia 
                        ?p - primero
                        ?t - tipo_plato
                        ?x - segundo
                        ?c - dia 
                        ?tc - tipo_plato 
                        ) 
           :precondition (and 
                           (not (servido_primero ?d))
                           (not (preparado ?p))

                           (servido_segundo ?d)
                           (menu_segundo ?d ?x)
                           (not (incompatible ?p ?x))

                           (es_de_tipo ?p ?t)
                           (consecutivo ?d ?c)
                           (servido_primero ?c)
                           (tipo_dia_primero ?c ?tc)
                           (not (= ?t ?tc))

                           (> (+ (calorias ?p) (calorias ?x)) 1000)
                           (< (+ (calorias ?p) (calorias ?x)) 1500)

                           )
           :effect  (and
                      (servido_primero ?d)
                      (preparado ?p)
                      (tipo_dia_primero ?d ?t)
                      (menu_primero ?d ?p)

                      (increase (precio_total) (precio_plato ?p))
                      )
           )

  ; Lo mismo que servir_primero pero con los roles invertidos.
  (:action servir_segundo
           :parameters (
                        ?d - dia 
                        ?p - segundo
                        ?t - tipo_plato
                        ?x - primero
                        ?c - dia 
                        ?tc - tipo_plato 
                        ) 
           :precondition (and 
                           (servido_primero ?d)
                           (not (servido_segundo ?d))
                           (not (preparado ?p))
                           (menu_primero ?d ?x)
                           (not (incompatible ?x ?p))

                           (es_de_tipo ?p ?t)
                           (consecutivo ?d ?c)

                           (servido_segundo ?c)
                           (tipo_dia_segundo ?c ?tc)

                           (not (= ?t ?tc))

                           (> (+ (calorias ?p) (calorias ?x)) 1000)
                           (< (+ (calorias ?p) (calorias ?x)) 1500)

                           )
           :effect  (and
                      (servido_segundo ?d)
                      (preparado ?p)
                      (tipo_dia_segundo ?d ?t)
                      (menu_segundo ?d ?p)

                      (increase (precio_total) (precio_plato ?p))
                      )
           )
  )
