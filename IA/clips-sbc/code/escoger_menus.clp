(deffunction escoger_menus::imprimir_plato (?platoPonderado ?nombreOrdinal)
  "Funcion auxiliar usada para imprimir un plato y su informacion"
  (bind ?plato (send ?platoPonderado get-plato))
  (bind ?nombre (send ?plato get-nombre_plato))
  (bind ?precio (send ?plato get-precio))
  (bind $?justificaciones (send ?platoPonderado get-justificaciones))

  (printout t (format nil 
    "%s: %s" ?nombreOrdinal ?nombre) crlf)

  (printout t (format nil 
    "  Precio: %d EUR " ?precio) crlf)

  (printout t "Justificaciones:" crlf)
	(progn$ (?just $?justificaciones)
		(printout t (format nil "  > %s" ?just) crlf)
	)
)

(deffunction escoger_menus::imprimir_bebida 
  "Funcion auxiliar usada para imprimir una bebida y su informacion"
  (?bebidaPonderada ?nombreOrdinal)
  (bind ?bebida (send ?bebidaPonderada get-bebida))
  (bind ?nombre (send ?bebida get-nombre_bebida))
  (bind ?precio (send ?bebida get-precio))

  (printout t (format nil 
    "Bebida del %s: %s" ?nombreOrdinal ?nombre) crlf)

  (printout t (format nil 
    "  Precio: %d EUR " ?precio) crlf)
)

(deffunction escoger_menus::imprimir_menu (?menu ?puntuacion)
  "Funcion usada para imprimir un menu integramente y su informacion"
  (bind ?primeroP (send ?menu get-primer_plato))
  (bind ?segundoP (send ?menu get-segundo_plato))
  (bind ?postreP (send ?menu get-postre))

  (bind ?bebidaP_primero (send ?menu get-bebida_primer_plato))
  (bind ?bebidaP_segundo (send ?menu get-bebida_segundo_plato))
  (bind ?bebidaP_postre (send ?menu get-bebida_postre))

  (bind ?precio (send ?menu get-precio))
  (bind $?justificaciones (send ?menu get-justificaciones))

  (imprimir_plato ?primeroP "Primer plato")
  (imprimir_bebida ?bebidaP_primero "Primer plato")
  (printout t "---------------------------------" crlf)
  (imprimir_plato ?segundoP "Segundo plato")
  (imprimir_bebida ?bebidaP_segundo "Segundo plato")
  (printout t "---------------------------------" crlf)
  (imprimir_plato ?postreP "Postre")
  (imprimir_bebida ?bebidaP_postre "Postre")
  (printout t "---------------------------------" crlf)
  (printout t (format nil 
    "Precio del menu: %d euros" ?precio) crlf)
  (printout t (format nil 
    "Puntuacion del menu: %d puntos" ?puntuacion) crlf)

  (printout t "---------------------------------" crlf)

  (printout t "Justificaciones de la eleccion del menu:" crlf)
	(progn$ (?curr-just $?justificaciones)
		(printout t (format nil "  > %s" ?curr-just) crlf)
	)
)

(defrule escoger_menus::inicializar_menus_barato_caro
  "Regla auxiliar usada para inicializar los menus mas barato, caro y el
  estandar para posterior uso"
  (not (menusInicializados))
	?menu <- (object (is-a Menu) 
              (puntuacion ?punt)
              (precio ?precio)
            )
  (datos_comensal (precio_minimo ?precioMin) (precio_maximo ?precioMax))
  =>
  (bind ?difPrecioB ( * (- ?precio ?precioMin) 1))
  (bind ?difPrecioC ( * (- ?precioMax ?precio) 1))
  (bind ?puntBarato (- ?punt ?difPrecioB))
  (bind ?puntCaro (- ?punt ?difPrecioC))

  (assert (menuMasBarato ?menu ?puntBarato ?difPrecioB))
  (assert (menuMasCaro ?menu ?puntCaro ?difPrecioC))
  (assert (menuMasNormal ?menu ?punt))
  (assert (menusInicializados))
)

(defrule escoger_menus::generar_menu_mas_barato
  "Regla que genera el menu mas barato y decente de entre todos los posibles"
	?menu <- (object (is-a Menu) 
              (puntuacion ?punt)
              (precio ?precio)
            )
  (datos_comensal (precio_minimo ?precioMin))
  ?h <- (menuMasBarato ?menuBarato ?puntMasBarato ?difPrecio)
  =>

  (bind ?difPrecio ( * (- ?precio ?precioMin) 1))
  (bind ?puntBarato (- ?punt ?difPrecio))

  (if
    (> ?puntBarato ?puntMasBarato)
  then
    (retract ?h)
    (assert (menuMasBarato ?menu ?puntBarato ?difPrecio))
  )
)

(defrule escoger_menus::generar_menu_mas_caro
  "Regla que genera el menu mas caro y mejor de entre todos los posibles"
	?menu <- (object (is-a Menu) 
              (puntuacion ?punt)
              (precio ?precio)
            )
  (datos_comensal (precio_maximo ?precioMax))
  ?h <- (menuMasCaro ?menuCaro ?puntMasCaro ?difPrecio)
  =>
  (bind ?difPrecio ( * (- ?precioMax ?precio) 1))
  (bind ?puntCaro (- ?punt ?difPrecio))

  (if
    (> ?puntCaro ?puntMasCaro)
  then
    (retract ?h)
    (assert (menuMasCaro ?menu ?puntCaro ?difPrecio))
  )
)

(defrule escoger_menus::generar_menu_mas_normal
  "Regla que genera el menu estandar sin mirar el precio"
	?menu <- (object (is-a Menu) 
              (puntuacion ?punt)
            )
  ?h <- (menuMasNormal ?menuNormal ?puntMasNormal)
  =>
  (if
    (> ?punt ?puntMasNormal)
  then
    (retract ?h)
    (assert (menuMasNormal ?menu ?punt))
  )
)

(defrule escoger_menus::imprimir_menues
  "Regla que imprime los mejores menus. Es la ultima regla del sistema
  que es disparada"
  (declare (salience -10))
  (menuMasBarato ?menuBarato ?puntuacionBarato ?difPrecioB) 
  (menuMasCaro ?menuCaro ?puntuacionCaro ?difPrecioC)
  (menuMasNormal ?menuNormal ?puntuacionNormal)
  =>
  (printout t crlf crlf)
  (printout t "-------------..oOOo..------------" crlf)
  (printout t "-          MENU ECONOMICO       -" crlf)
  (printout t "-------------**uUUu**------------" crlf)
  (printout t crlf)
  (imprimir_menu ?menuBarato ?puntuacionBarato)
  (printout t (format nil "  > Precio: -%d" ?difPrecioB) crlf)
  (printout t crlf)

  (printout t "-------------..oOOo..------------" crlf)
  (printout t "-           MENU DELUXE         -" crlf)
  (printout t "-------------**uUUu**------------" crlf)
  (printout t crlf)
  (imprimir_menu ?menuCaro ?puntuacionCaro)
  (printout t (format nil "  > Precio: -%d" ?difPrecioC) crlf)
  (printout t crlf)
  (printout t "-------------..oOOo..------------" crlf)
  (printout t "-           MENU ESTANDAR       -" crlf)
  (printout t "-------------**uUUu**------------" crlf)
  (printout t crlf)
  (imprimir_menu ?menuNormal ?puntuacionNormal)
  (printout t crlf)
)
