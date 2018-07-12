;Template para los datos de nuestro Comensal
(deftemplate MAIN::datos_comensal
  ; Nombre comensal
  (slot nombre (type STRING))
  ; Precio minimo que quiere gastarse
  (slot precio_minimo
    (type INTEGER)
  )
  ; Precio maximo que quiere gastarse
  (slot precio_maximo
    (type INTEGER)
  )
  ; Es vegetariano
  (slot vegetariano)
  ; Si quiere prohibir algunos ingredientes
  (slot quiere_prohibir)
  ; El conjunto de ingredientes que el comensal no quiere comer
  (multislot ingredientes_prohibidos
    (type INSTANCE)
  )
  ; Si quiere vino
  (slot quiere_vino)
  ; Si quiere un vino acorde para cada plato
  (slot quiere_vino_para_cada_plato)
  ; El tipo de comida que le gusta
  (slot tipo_comida
    (type STRING)
    (allowed-strings "Tradicional" "Moderno" "Sibarita")
  )
  ; Epoca del ano en la que estamos
  (slot epoca_ano
    (allowed-strings "Primavera" "Verano" "Otonio" "Invierno")
  )
  ; El numero de comensales que van a asistir a la comida
  (slot numero_comensales
    (type INTEGER)
  )
  ; Si prefiere un menu infantil
  (slot infantil)
)

(defrule MAIN::initialRule 
  "Regla inicial"
  =>
	(printout t
    "====================================================================" crlf)
  (printout t
    "=                Sistema de recomendacion de menus                 =" crlf)
  (printout t
    "====================================================================" crlf)
  (printout t crlf)
  (printout t "Bienvenido al sistema de recomendacion RicoRico" crlf)

	(printout t" __________  " crlf)
	(printout t"< RicoRico! > " crlf)
	(printout t" ----------  " crlf)
	(printout t"    \\        " crlf)
	(printout t"     \\  /\\/\\ " crlf)
	(printout t"       \\   / " crlf)
	(printout t"       |  0 >" crlf)>
	(printout t"       |___| " crlf)
	(printout t" __((_<|   | " crlf)
	(printout t"(          | " crlf)
	(printout t"(__________) " crlf)
	(printout t"   |      |  " crlf)
	(printout t"   |      |  " crlf)
	(printout t"   /\\     /\\ " crlf)

  (printout t
    "A continuacion se le realizaran una serie de preguntas" crlf)
  (printout t
    "para recomendarle menues" crlf)
  (printout t crlf)
  (focus obtener_preferencias)
)
