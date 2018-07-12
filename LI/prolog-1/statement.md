1. Escribe un predicado prolog "flatten" que aplana listas:

?- flatten( [a,[b,c,[b,b],e], f], L).
L = [a,b,c,b,b,e,f]

Escribe otro que elimina las repeticiones:
?- flattenNoRepetitions( [a,[b,c,[b,b],e], f], L).
L = [a,b,c,e,f]

2. Tenemos una fila de cinco casas, con cinco vecinos con casas de colores diferentes, y cinco 
profesiones, animales, bebidas y nacionalidades diferentes, y sabiendo que:

    1 - El que vive en la casa roja es de Peru
    2 - Al frances le gusta el perro
    3 - El pintor es japones
    4 - Al chino le gusta el ron
    5 - El hungaro vive en la primera casa
    6 - Al de la casa verde le gusta el coñac
    7 - La casa verde esta a la izquierda de la blanca
    8 - El escultor cría caracoles
    9 - El de la casa amarilla es actor
   10 - El de la tercera casa bebe cava
   11 - El que vive al lado del actor tiene un caballo
   12 - El hungaro vive al lado de la casa azul
   13 - Al notario la gusta el whisky
   14 - El que vive al lado del medico tiene un ardilla,

Escribe un programa Prolog que averigue para cada persona todas sus 
caracteristicas de la forma num\_casa,color,profesion,animal,bebida,pais] 
averiguables. Ayuda: sigue el siguiente esquema:

casas:-	Sol = [	[1,A1,B1,C1,D1,E1],
		[2,A2,B2,C2,D2,E2],
		[3,A3,B3,C3,D3,E3],
		[4,A4,B4,C4,D4,E4],
		[5,A5,B5,C5,D5,E5] ],
        member(  ... , Sol),
        ...
	write(Sol), nl.



3. En el lenguaje de programacion "sumbol" un programa tiene la siguiente sintaxis:

<programa>  		-->    begin  <instrucciones>  end

<instrucciones>  	-->    <instruccion>
<instrucciones>  	-->    <instruccion> ; <instrucciones>  

<instruccion>  		-->    <variable> = <variable> + <variable>
<instruccion>  		-->    if <variable> = <variable> then <instrucciones> 
				 else <instrucciones>  endif
<variable>		-->    x
<variable>		-->    y
<variable>		-->    z

Tres ejemplos de programas sumbol:
  begin x=x+z end
  begin x=x+y; z=z+z; x=y+x end
  begin x=y+z; if z=z then x=x+z; y=y+z else z=z+y endif; x=x+z end 

Escribe en Prolog un sencillo analizador sintactico para el lenguaje
sumbol, es decir, que se comporte así:

?- programa( [begin, z, =, x, +, y, end] ).
yes
?- programa( [begin, z, =, x, +, y, ;, x, =, z, z, end] ).
no

(en el segundo ejemplo falta un "+").  
Para ello, haz una clausula Prolog para cada regla de la gramatica, usando appends para
separar las partes de la entrada (obligatorio).

