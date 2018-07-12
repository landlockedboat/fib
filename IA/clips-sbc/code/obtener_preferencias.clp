; Funcion para hacer una pregunta con respuesta cualquiera
(deffunction obtener_preferencias::pregunta_general (?pregunta)
  (printout t crlf)
  (format t "%s " ?pregunta)
	(bind ?respuesta (read))
	(while (not (lexemep ?respuesta)) do
		(format t "%s " ?pregunta)
		(bind ?respuesta (read))
    )
	?respuesta
)

; Funcion para hacer una pregunta general con una serie de respuestas admitidas
(deffunction obtener_preferencias::pregunta_opciones (?question $?allowed_values)
   (printout t crlf)
   (format t "%s "?question)
   (progn$ (?curr_value $?allowed_values)
		(format t "[%s]" ?curr_value)
	)
   (printout t ": ")
   (bind ?answer (read))
   (if (lexemep ?answer)
       then (bind ?answer (lowcase ?answer)))
   (while (not (member ?answer ?allowed_values)) do
      (format t "%s "?question)
	  (progn$ (?curr_value $?allowed_values)
		(format t "[%s]" ?curr_value)
	  )
	  (printout t ": ")
      (bind ?answer (read))
      (if (lexemep ?answer)
          then (bind ?answer (lowcase ?answer))))
   ?answer
)

; Funcion para hacer una pregunta de tipo si/no
(deffunction obtener_preferencias::pregunta_si_no (?question)
   (bind ?response (pregunta_opciones ?question si no))
   (if (or (eq ?response si) (eq ?response s))
       then TRUE
       else FALSE)
)

; Funcion para hacer una pregunta con respuesta numerica unica
(deffunction obtener_preferencias::pregunta_numerica (?pregunta ?rangini ?rangfi)
  (printout t crlf)
	(format t "%s [%d, %d] " ?pregunta ?rangini ?rangfi)
	(bind ?respuesta (read))
	(while (not(and(>= ?respuesta ?rangini)(<= ?respuesta ?rangfi))) do
		(format t "%s [%d, %d] " ?pregunta ?rangini ?rangfi)
		(bind ?respuesta (read))
	)
	?respuesta
)

; Funcion para hacer pregunta con indice de respuestas posibles
(deffunction obtener_preferencias::pregunta_indice (?pregunta $?valores_posibles)
    (bind ?linea (format nil "%s" ?pregunta))
    (printout t crlf)
    (printout t ?linea crlf)
    (progn$ (?var ?valores_posibles)
            (bind ?linea (format nil "  %d. %s" ?var-index ?var))
            (printout t ?linea crlf)
    )
    (bind ?indice (pregunta_numerica "Escoja una opcion:" 1 (length$ ?valores_posibles)))
  	(bind ?respuesta (nth$ ?indice $?valores_posibles))
    ?respuesta
)

; Funcion para hacer una pregunta multi-respuesta con indices
(deffunction obtener_preferencias::pregunta_multi (?pregunta $?valores_posibles)
    (bind ?linea (format nil "%s" ?pregunta))
    (printout t crlf)
    (printout t ?linea crlf)
    (progn$ (?var ?valores_posibles)
            (bind ?linea (format nil "  %d. %s" ?var-index ?var))
            (printout t ?linea crlf)
    )
    (format t "%s" "Indique los numeros separados por un espacio: ")
    (bind ?resp (readline))
    (bind ?numeros (str-explode ?resp))
    (bind $?lista (create$ ))
    (progn$ (?var ?numeros)
        (if (and (integerp ?var) (and (>= ?var 1) (<= ?var (length$ ?valores_posibles))))
            then
                (if (not (member$ ?var ?lista))
                    then (bind ?lista (insert$ ?lista (+ (length$ ?lista) 1) ?var))
                )
        )
    )
    ?lista
)

(deffacts obtener_preferencias::hechos_iniciales
  "Establece hechos para poder recopilar informacion"
  ; Pregunta por:
  ; El nombre
  (nombre_comensal ask)
  ; Precio minimo que quiere gastarse
  (precio_minimo ask)
  ; Precio maximo que quiere gastarse
  (precio_maximo ask)
  ; Si prefiere un menu vegetariano
  (vegetariano ask)
  ; Si quiere prohibir algunos ingredientes
  (quiere_prohibir ask)
  (ingredientes_prohibidos ask)
  ; Si quiere vino
  (quiere_vino ask)
  ; Si quiere un vino acorde para cada plato
  (quiere_vino_para_cada_plato ask)
  ; El tipo de comida que le gusta
  (tipo_comida ask)
  ; Epoca del ano en la que estamos
  (epoca_ano ask)
  ; Numero de comensales que van a asistir al evento
  (numero_comensales ask)
  ; Si el usuario quiere un menu infantil
  (infantil ask)
  ; Entonces establecemos datos_comensal como "cierto"
  (datos_comensal)
)

(defrule obtener_preferencias::establecer_nombre
  "Establece el nombre del comensal, es la primera pregunta"
  ?h <- (nombre_comensal ask)
  ?dat <- (datos_comensal)
  =>
  (printout t "---------------------------------" crlf)
  (bind ?nombre (pregunta_general "Como se llama? "))

  (retract ?h)
  (assert (nombre_comensal TRUE))
  (modify ?dat (nombre ?nombre))
)

(defrule obtener_preferencias::establecer_precio_minimo
  "Establece el precio minimo que el comensal esta dispuesto a pagar"
  ?h <- (precio_minimo ask)
  ?dat <- (datos_comensal)
  (nombre_comensal TRUE)
  =>
  (printout t "---------------------------------" crlf)
  (bind ?precio
    (pregunta_numerica
      "Cual es el precio minimo que quiere pagar por menu?" 0 999)
  )

  (retract ?h)
  (assert (precio_minimo TRUE))
  (modify ?dat (precio_minimo ?precio))
)

(defrule obtener_preferencias::establecer_precio_maximo
  "Establece el precio maximo que el comensal esta dispuesto a pagar"
  ?h <- (precio_maximo ask)
  ?dat <- (datos_comensal)
  (precio_minimo TRUE)
  (datos_comensal (precio_minimo ?precioMin))
  =>
  (printout t "---------------------------------" crlf)
  (bind ?precio
    (pregunta_numerica
      "Cual es el precio maximo que quiere pagar por menu?" 0 999)
  )

  (if (> ?precioMin ?precio )
    then
    (printout t
      "El precio minimo no puede ser mayor al precio maximo" crlf)
    (printout t
      "Vuelve a intentarlo" crlf)
    else
    (retract ?h)
    (assert (precio_maximo TRUE))
    (modify ?dat (precio_maximo ?precio))
  )
)

(defrule obtener_preferencias::establecer_vegetariano
  "Determina si el comensal es vegetariano"
  ?h <- (vegetariano ask)
  ?dat <- (datos_comensal)
  (precio_maximo TRUE)
  =>
  (printout t "---------------------------------" crlf)
  (bind ?respuesta (pregunta_si_no "Prefiere un menu vegetariano?"))
  (retract ?h)
  (assert (vegetariano TRUE))
  (modify ?dat (vegetariano $?respuesta))
)

(defrule obtener_preferencias::establecer_quiere_prohibir
  "Determina si el comensal quiere prohibir ciertos ingredientes"
  ?h <- (quiere_prohibir ask)
  ?dat <- (datos_comensal)
  (vegetariano TRUE)
  =>
  (printout t "---------------------------------" crlf)
  (bind ?respuesta (pregunta_si_no "Quiere prohibir algunos ingredientes?"))
  (retract ?h)
  (assert (quiere_prohibir TRUE))
  (modify ?dat (quiere_prohibir ?respuesta))
)

(defrule obtener_preferencias::establecer_ingredientes_prohibidos
  "Establece los ingredientes prohibidos por el comensal"
  (quiere_prohibir TRUE)
  ?h <- (ingredientes_prohibidos ask)
  ?dat <- (datos_comensal)
  (datos_comensal (quiere_prohibir TRUE))
  =>
  (printout t "---------------------------------" crlf)
  (bind $?obj_ingredientes
    (find-all-instances ((?inst Ingrediente)) TRUE)
  )
  ; La descripcion de los ingredientes que se imprimira por pantalla
  (bind $?desc_ing (create$))

  (loop-for-count (?i 1 (length$ $?obj_ingredientes)) do
    (bind ?curr_obj (nth$ ?i ?obj_ingredientes))
    (bind ?curr_nom (send ?curr_obj get-nombre_ingrediente))
    ; Una vez tenemos todos los datos de nuestro
    ; ingrediente, lo concatenamos con la descripcion
    ; de todos ellos
    (bind $?desc_ing
      (insert$ $?desc_ing
        ( + 1 (length$ $?desc_ing) ) ?curr_nom )
    )
  )

  (bind $?respuestas (create$))

  (bind $?prohibidos
    (pregunta_multi
      "Escriba los ingredientes que no quiere ver en sus menues: "
      $?desc_ing)
  )
  (printout t crlf)

  (loop-for-count (?i 1 (length$ $?prohibidos)) do
    (bind ?prohibido (nth$ ?i $?prohibidos))
    (if (<= ?prohibido (length$ $?obj_ingredientes)) then
      (bind ?curr_obj (nth$ ?prohibido $?obj_ingredientes))
      (bind $?respuestas
        (insert$ $?respuestas
            ( + (length$ $?respuestas) 1) ?curr_obj )
      )
    )
  )
  
  (retract ?h)
  (assert (ingredientes_prohibidos TRUE))
  (modify ?dat (ingredientes_prohibidos $?respuestas))
)

(defrule obtener_preferencias::establecer_quiere_vino
  "Determina si el comensal quiere que se incluya vino en el menu"
  ?h <- (quiere_vino ask)
  ?dat <- (datos_comensal)
  ; If quiere_prohibir has already been answered
  (quiere_prohibir TRUE)
  =>
  (printout t "---------------------------------" crlf)
  (bind ?respuesta (pregunta_si_no "Quiere vino en el menu?"))

  (retract ?h)
  (assert (quiere_vino TRUE))
  (modify ?dat (quiere_vino $?respuesta))
)

(defrule obtener_preferencias::establecer_quiere_vino_para_cada_plato
  "Determina si el comensal quiere que cada plato tenga un vino distinto"
  ?h <- (quiere_vino_para_cada_plato ask)
  ?dat <- (datos_comensal)
  (quiere_vino TRUE)
  (datos_comensal (quiere_vino ?quiere_vino))
  =>
  ; Comprobamos si el comensal queria vino, si queria vino preguntamos
  ; por vino en cada plato. Incondicionalmente, ponemos el booleano
  ; de preguntar por el vino para cada plato a cierto.
  (if (eq ?quiere_vino TRUE)
  then
    (printout t "---------------------------------" crlf)
    (bind ?respuesta (
      pregunta_si_no "Quiere que el vino sea distinto para cada plato?")
    )
    (modify ?dat (quiere_vino_para_cada_plato $?respuesta))
  )
  (retract ?h)
  (assert (quiere_vino_para_cada_plato TRUE))
)

(defrule obtener_preferencias::establecer_tipo_comida
  "Establece el tipo de comida que le gusta al comensal"
  ?h <- (tipo_comida ask)
  ?dat <- (datos_comensal)
  (quiere_vino_para_cada_plato TRUE)
	=>
  (printout t "---------------------------------" crlf)
	(bind ?alternativas (create$ "Tradicional" "Moderno" "Sibarita"))
	(bind ?respuesta (
    pregunta_indice "Que tipo de comida te gusta?" ?alternativas)
  )

	(retract ?h)
	(assert (tipo_comida TRUE))
  (modify ?dat (tipo_comida ?respuesta))
)

(defrule obtener_preferencias::establecer_epoca_ano
  "Establece en que epoca del ano estamos"
  ?h <- (epoca_ano ask)
  ?dat <- (datos_comensal)
  (tipo_comida TRUE)
	=>
  (printout t "---------------------------------" crlf)
	(bind ?alternativas (create$ "Primavera" "Verano" "Otonio" "Invierno"))
	(bind ?respuesta (
    pregunta_indice "En que epoca del ano va a celebrarse la comida?" ?alternativas)
  )

	(retract ?h)
	(assert (epoca_ano TRUE))
  (modify ?dat (epoca_ano ?respuesta))
)

(defrule obtener_preferencias::establecer_numero_comensales
  "Establece el numero de comensales que van a asistir"
  ?h <- (numero_comensales ask)
  ?dat <- (datos_comensal)
  (epoca_ano TRUE)
  =>
  (printout t "---------------------------------" crlf)
  (bind ?num
    (pregunta_numerica
      "Cual es el numero de comensales que asistiran" 0 999)
  )

  (retract ?h)
  (assert (numero_comensales TRUE))
  (modify ?dat (numero_comensales ?num))
)

(defrule obtener_preferencias::establecer_infantil
  "Determina si el comensal prefiere generar un menu infantil"
  ?h <- (infantil ask)
  ?dat <- (datos_comensal)
  (numero_comensales TRUE)
  =>
  (printout t "---------------------------------" crlf)
  (bind ?respuesta (pregunta_si_no "Prefiere un menu infantil?"))
  (retract ?h)
  (assert (infantil TRUE))
  (modify ?dat (infantil $?respuesta))
)

(defrule obtener_preferencias::siguiente_modulo
  "Pasa al siguiente modulo una vez todas las reglas se han ejecutado"
  (declare (salience -10))
  =>
  (focus inferir_preferencias)
)
