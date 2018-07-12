(defrule descartar_platos::crear_listas
  "Regla auxiliar final para crear listaPlatos y listaBebidas"
  (declare (salience 10))
  (not (listasCreadas))
  =>
  (assert (listaPlatos (find-all-instances ((?inst Plato)) TRUE)))
  (assert (listaBebidas (find-all-instances ((?inst Bebida)) TRUE)))
  (assert (listasCreadas))
)

(defrule descartar_platos::descartar_vinos
  "Descartamos los vinos si el usuario prefiere que no se sirva vino"
  (not (refinar_vinos))
  (datos_comensal (quiere_vino FALSE))
  ?l <- (listaBebidas $?listaBebidas)
  =>
  (progn$ (?bebida $?listaBebidas)
    (bind ?nombreBebida (send ?bebida get-nombre_bebida))
    (if (eq (type ?bebida) Vino) then
      ; Informacion de debug
      ; (printout t
      ;  (format nil "Eliminando %s porque es vino." ?nombreBebida) crlf )
      (bind ?listaBebidas (delete-member$ ?listaBebidas ?bebida))
    )
  )

  (retract ?l)
  (assert (listaBebidas ?listaBebidas))
  (assert (refinar_vinos))
)

(defrule descartar_platos::descartar_ingredientes_con_carne
  "Descarta los platos que contienen ingredientes con carne si el comensal es vegetariano"
  (not (refinar_vegetariano))
  (datos_comensal (vegetariano TRUE))
  ?l <- (listaPlatos $?listaPlatos)
  =>
  (progn$ (?plato $?listaPlatos)
    (bind $?ingredientes (send ?plato get-compuesto_por))
    (progn$ (?ing $?ingredientes)
      (bind ?esAnimal (send ?ing get-animal))
      (bind ?nombrePlato (send ?plato get-nombre_plato))
      (if
        (eq ?esAnimal TRUE)
      then
        ; Informacion de debug
        ; (printout t
        ;  (format nil "Eliminando %s porque contiene carne." ?nombrePlato) crlf )
        (bind ?listaPlatos (delete-member$ ?listaPlatos ?plato))
        (break)
      )
    )
  )
  (retract ?l)
  (assert (listaPlatos ?listaPlatos))
  (assert (refinar_vegetariano))
)

(defrule descartar_platos::descartar_ingredientes_prohibidos
  "Descarta los platos que contienen ingredientes prohibidos por el usuario"
  (not (refinar_ingredientes_prohibidos))
  (datos_comensal 
    (quiere_prohibir TRUE) 
    (ingredientes_prohibidos $?ingredientes_prohibidos) 
  )
  ?l <- (listaPlatos $?listaPlatos)
  =>
  (progn$ (?plato $?listaPlatos)
    (bind ?nombrePlato (send ?plato get-nombre_plato))
    (bind $?ingredientes (send ?plato get-compuesto_por))
    (progn$ (?ing $?ingredientes)
      (bind ?nombreIng (send ?ing get-nombre_ingrediente))
      (bind ?eliminado FALSE)
        (progn$ (?prohibido $?ingredientes_prohibidos)
          (bind ?nombreProhibido (send ?prohibido get-nombre_ingrediente))
          (if
            (eq ?nombreIng ?nombreProhibido)
          then
            ; Informacion de debug
            ; (printout t (format nil 
            ;  "Eliminando %s porque contiene %s." 
            ;  ?nombrePlato ?nombreProhibido) crlf )
            (bind ?listaPlatos (delete-member$ ?listaPlatos ?plato))
            (bind ?eliminado TRUE)
            (break)
          )
          (if (eq ?eliminado TRUE) then (break))
        )
      (if (eq ?eliminado TRUE) then (break))
    )
  )
  (retract ?l)
  (assert (listaPlatos ?listaPlatos))
  (assert (refinar_ingredientes_prohibidos))
)

(defrule descartar_platos::descartar_platos_dificiles
  "Descarta los platos dificiles si el numero de comensales es elevado"
  (not (refinar_dificiles))
  (datos_comensal (numero_comensales ?num))
  (test (>= ?num 20))
  ?l <- (listaPlatos $?listaPlatos)
  =>
  (progn$ (?plato $?listaPlatos)
    (bind ?nombrePlato (send ?plato get-nombre_plato))
    (bind ?dificil (send ?plato get-dificil))
    (if
      (eq ?dificil FALSE)
    then
      ; Informacion de debug
      ; (printout t
      ;  (format nil "Eliminando %s porque es dificil." ?nombrePlato) crlf )
      (bind ?listaPlatos (delete-member$ ?listaPlatos ?plato))
    )
  )
  (retract ?l)
  (assert (listaPlatos ?listaPlatos))
  (assert (refinar_dificiles))
)

(defrule descartar_platos::descartar_platos_no_infantiles
  "Descarta los platos no infantiles si el comensal ha pedido menu infantil"
  (not (refinar_infantil))
  (datos_comensal (infantil TRUE))
  ?l <- (listaPlatos $?listaPlatos)
  =>
  (progn$ (?plato $?listaPlatos)
    (bind ?nombrePlato (send ?plato get-nombre_plato))
    (bind ?infantil (send ?plato get-infantil))
    (if
      (eq ?infantil FALSE)
    then
      ; Informacion de debug
      ; (printout t
      ;  (format nil "Eliminando %s porque no es infantil." ?nombrePlato) crlf )
      (bind ?listaPlatos (delete-member$ ?listaPlatos ?plato))
    )
  )
  (retract ?l)
  (assert (listaPlatos ?listaPlatos))
  (assert (refinar_infantil))
)

(defrule descartar_platos::siguiente_modulo
  "Pasa al siguiente modulo una vez todas las reglas se han ejecutado"
  (declare (salience -10))
  =>
  (focus puntuar_platos)
)
