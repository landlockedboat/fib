; Este modulo infiere preferencias adicionales a traves de lo que el
; comensal ha introducido en el sistema.

; Si uno de los hechos inferidos tiene el valor TRUE signigica que al
; comensal le gustan. Por el contrario, si tienen como valor FALSE
; significa que al comensal le gustaria evitar esos platos dadas
; las circunstancias.

(defrule inferir_preferencias::inferir_cualidades_sibarita
  "Infiere las preferencias asociadas a un comensal sibarita"
  (datos_comensal (tipo_comida "Sibarita"))
  =>
  ; A los comensales sibaritas les gustan los platos de temporada
  (assert (platos_temporada TRUE))
  ; A los comensales sibaritas les gustan los platos "exclusivos"
  (assert (platos_exclusivos TRUE))
  ; A los comensales sibaritas les gustan los platos extranjeros
  (assert (platos_extranjeros TRUE))
)

(defrule inferir_preferencias::inferir_cualidades_tradicional
  "Infiere las preferencias asociadas a un comensal de gustos tradicionales"
  (datos_comensal (tipo_comida "Tradicional"))
  =>
  (assert (platos_tradicionales TRUE))
  (assert (platos_pesados TRUE))
  (assert (platos_espanoles TRUE))
  (assert (platos_grandes TRUE))

  ; A los comensales tradicionales no les gustan los platos con 
  ; raciones pequeñas
  (assert (platos_pequenos FALSE))
)

(defrule inferir_preferencias::inferir_cualidades_moderno
  "Infiere las preferencias asociadas a un comensal de gustos modernos"
  (datos_comensal (tipo_comida "Moderno"))
  =>
  (assert (platos_modernos TRUE))
  (assert (platos_extranjeros TRUE))
  (assert (platos_pequenos TRUE))

  (assert (platos_espanoles FALSE))
  (assert (platos_pesados FALSE))

)

(defrule inferir_preferencias::inferir_cualidades_verano
  "Infiere preferencias asociadas con comer durante el verano"
  (datos_comensal (epoca_ano "Verano"))
  =>
  (assert (platos_calientes FALSE))
)

(defrule inferir_preferencias::inferir_cualidades_invierno
  "Infiere preferencias asociadas con comer durante el invierno"
  (datos_comensal (epoca_ano "Invierno"))
  =>
  (assert (platos_calientes TRUE))
)

(defrule inferir_preferencias::siguiente_modulo
  "Pasa al siguiente modulo una vez todas las reglas se han ejecutado"
  (declare (salience -10))
  =>
  (focus descartar_platos)
)
