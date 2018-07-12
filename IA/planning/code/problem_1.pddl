(define (problem menus-1)
  (:domain menus)
  (:objects
    ; Primeros
    paella ensalada sopa macarrones arroz tortilla fabada
    empanada callos migas calsots - primero
    ; Segundos
    lubina salmon entrecot salsichas setas huevo_frito pizza
    lechazo boquerones cordero - segundo
    ; Dias
    lunes martes miercoles jueves viernes - dia
    sopa crema ensalada carne pescado pasta arroz huevo - tipo_plato
    )
  (:init

    (consecutivo lunes martes)
    (consecutivo martes miercoles)
    (consecutivo miercoles jueves)
    (consecutivo jueves viernes)
    ; No usamos este ultimo predicado porque resulta en un ciclo.
    ;(consecutivo viernes lunes)

    ; Usamos en su lugar este predicado que indica que viernes no
    ; tiene sucesor.
    (ultimo_dia viernes)

    ; NIVEL BASICO
    ; Incompatibilidades 
    (incompatible paella lubina)
    (incompatible paella salmon)
    (incompatible ensalada salmon)
    (incompatible sopa entrecot)
    (incompatible tortilla huevo_frito)
    (incompatible fabada salmon)
    (incompatible sopa pizza)
    (incompatible paella setas) 
    (incompatible arroz salsichas)
    (incompatible ensalada setas)


    ; EXTENSION 2
    ; Tipos de los platos
    (es_de_tipo paella pescado)
    (es_de_tipo ensalada ensalada)
    (es_de_tipo sopa sopa)
    (es_de_tipo macarrones pasta)
    (es_de_tipo arroz arroz)
    (es_de_tipo tortilla huevo)
    (es_de_tipo fabada sopa)
    (es_de_tipo lubina pescado)
    (es_de_tipo salmon pescado) 
    (es_de_tipo entrecot carne)
    (es_de_tipo salsichas carne)
    (es_de_tipo setas ensalada)
    (es_de_tipo huevo_frito huevo)
    (es_de_tipo pizza carne)
    (es_de_tipo empanada carne)
    (es_de_tipo callos sopa)
    (es_de_tipo migas carne)
    (es_de_tipo calsots ensalada)
    (es_de_tipo lechazo carne)
    (es_de_tipo boquerones pescado)
    (es_de_tipo cordero carne)


    ; EXTENSION 3
    ; Esto se debe hacer por cada plato que deseemos fijar a un dia
    ; En este ejemplo, paella los jueve
    (servido_primero jueves)
    (menu_primero jueves paella)
    (tipo_dia_primero jueves pescado)
    (preparado paella)

    ; EXTENSION 4
    ; Calorias de los primeros platos
    (= (calorias paella) 600)
    (= (calorias ensalada) 400)
    (= (calorias sopa) 600)
    (= (calorias macarrones) 400)
    (= (calorias arroz) 400)
    (= (calorias tortilla) 400)
    (= (calorias fabada) 600)
    (= (calorias empanada) 400)
    (= (calorias callos) 400)
    (= (calorias migas) 600)
    (= (calorias calsots) 400)

    ; Calorias de los segundos platos
    (= (calorias lubina) 600)
    (= (calorias salmon) 1000)
    (= (calorias entrecot) 600)
    (= (calorias salsichas) 1000)
    (= (calorias setas) 1000)
    (= (calorias huevo_frito) 600)
    (= (calorias pizza) 1000)
    (= (calorias lechazo) 600)
    (= (calorias boquerones) 600)
    (= (calorias cordero) 1000)

    ; EXTENSION 5
    ; Precios de los primeros platos
    (= (precio_plato paella) 6)
    (= (precio_plato ensalada) 10)
    (= (precio_plato sopa) 6)
    (= (precio_plato macarrones) 6)
    (= (precio_plato arroz) 6)
    (= (precio_plato tortilla) 6)
    (= (precio_plato fabada) 10)
    (= (precio_plato empanada) 10)
    (= (precio_plato callos) 10)
    (= (precio_plato migas) 6)
    (= (precio_plato calsots) 6)

    ; Precios de los segundos platos
    (= (precio_plato lubina) 10)
    (= (precio_plato salmon) 10)
    (= (precio_plato entrecot) 15)
    (= (precio_plato salsichas) 10)
    (= (precio_plato setas) 15)
    (= (precio_plato huevo_frito) 10)
    (= (precio_plato pizza) 15)
    (= (precio_plato lechazo) 15)
    (= (precio_plato boquerones) 10)
    (= (precio_plato cordero) 15)

    (= (precio_total) 0)


    )

  (:metric 
    minimize (precio_total)
    )

  (:goal (forall (?d - dia)
                 (and (servido_primero ?d)
                      (servido_segundo ?d)
                      )
                 )
         )
  )
