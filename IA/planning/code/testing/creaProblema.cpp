#include <iostream>
#include <stdlib.h>     /* srand, rand */
#include <time.h>       /* time */
using namespace std;

string getDia(int i){
	string dia = "";
	
	switch ( i ) {
		case 0:
		  dia = "lunes";
		  break;
		case 1:
		  dia = "martes";
		  break;
		case 2:
		  dia = "miercoles";
		  break;
		case 3:
		  dia = "jueves";
		  break;
		case 4:
		  dia = "viernes";
		  break;
	}
	return dia;
}

int main(int argc, char const *argv[])
{
	srand (time(NULL));
	int numPrimersPlats = 50;
	int numSegonsPlats = 50;
	int numTipus = 6;


	int idPrimerPlatoRestriccion3 = rand()%numPrimersPlats;
	int idTipoPrimerPlatoRestriccion3;
	int idSegundoPlatoRestriccion3 = rand()%numSegonsPlats;
	int idTipoSegundoPlatoRestriccion3;
	cout << 
		"(define (problem menus-1)" << endl <<
		"  (:domain menus)" << endl <<
		"  (:objects" << endl <<
		
		"    ; Primeros" << endl
		<< "    ";
		for(int i = 0; i < numPrimersPlats; i++){
			cout << "primero" << i << " ";
		}cout << " - primero" << endl;

		cout << "    ; Segundos" << endl
		<< "    ";
		for(int i = 0; i < numSegonsPlats; i++){
			cout << "segundo" << i << " ";
		}cout << " - segundo" << endl;
		cout << "    ; Dias" << endl <<
				"    lunes martes miercoles jueves viernes - dia" << endl;

   cout << "    ; tipos" << endl
		<< "    ";
		for(int i = 0; i < numTipus; i++){
			cout << "tipo" << i << " ";
		}cout << " - tipo_plato" << endl;

	cout <<")" << endl <<
  	"(:init" << endl <<
  	endl <<
    "    (consecutivo lunes martes)" << endl <<
    "    (consecutivo martes miercoles)" << endl <<
    "    (consecutivo miercoles jueves)" << endl <<
    "    (consecutivo jueves viernes)" << endl <<
    "    ; No usamos este ultimo predicado porque resulta en un ciclo." << endl <<
    "    ;(consecutivo viernes lunes)" << endl <<
    "    ; Usamos en su lugar este predicado que indica que viernes no" << endl <<
    "    ; tiene sucesor." << endl <<
    "    (ultimo_dia viernes)" << endl <<
	endl <<
	"    ; NIVEL BASICO" << endl <<
	"    ; Incompatibilidades " << endl;
	for(int i = 0; i < numPrimersPlats; i++){
		for(int j = 0; j < numSegonsPlats; j++){
			if((rand()%101)<20)
			{
				cout << "    (incompatible primero" << i << " segundo"<< j << ")" << endl;
			}
		}
	}
    cout << 
	"    ; EXTENSION 2" << endl <<
	"    ; Tipos de los platos" << endl;
	for(int i = 0; i < numPrimersPlats; i++){
		int idTipo = rand()%numTipus;
		cout << "    (es_de_tipo primero" << i << " tipo" << idTipo << ")" << endl;
		if(i == idPrimerPlatoRestriccion3) idTipoPrimerPlatoRestriccion3 = idTipo;
	}
	for(int i = 0; i < numSegonsPlats; i++){
		int idTipo = rand()%numTipus;
		cout << "    (es_de_tipo segundo" << i << " tipo" << idTipo << ")" << endl;
		if(i == idSegundoPlatoRestriccion3) idTipoSegundoPlatoRestriccion3 = idTipo;
	}
	cout <<
	"	; EXTENSION 3" << endl <<
	"    ; Esto se debe hacer por cada plato que deseemos fijar a un dia" << endl <<
	"    ; en este caso obligaremos a hacer un primero y un segundo un dia de la semana" << endl;
	string dia = getDia(rand()%5);
	cout <<
	"    (servido_primero " << dia << ")" << endl <<
	"    (menu_primero " << dia << " primero" << idPrimerPlatoRestriccion3 << ")" << endl <<
	"    (tipo_dia_primero " << dia << " tipo" << idTipoPrimerPlatoRestriccion3 << ")" << endl <<
	"    (preparado primero" << idPrimerPlatoRestriccion3 << ")" << endl;
	dia = getDia(rand()%5);
	cout <<
	"    (servido_segundo " << dia << ")" << endl <<
	"    (menu_segundo " << dia << " segundo" << idSegundoPlatoRestriccion3 << ")" << endl <<
	"    (tipo_dia_segundo " << dia << " tipo" << idTipoSegundoPlatoRestriccion3 << ")" << endl <<
	"    (preparado segundo" << idSegundoPlatoRestriccion3 << ")" << endl;

	cout << 
	"	; EXTENSION 4" << endl <<
	"    ; Calorias de los primeros platos" << endl;
	for(int i = 0; i < numPrimersPlats; i++){
		int calorias = 500 + rand()%1001;
		cout << "    (= (calorias primero" << i << ") " << calorias <<")" << endl;
	}
	cout << endl <<
    "    ; Calorias de los segundos platos" << endl;
    for(int i = 0; i < numSegonsPlats; i++){
		int calorias = 500 + rand()%1001;
		cout << "    (= (calorias segundo" << i << ") " << calorias <<")" << endl;
	}
	cout <<
	"	; EXTENSION 5" << endl <<
	"    ; Precios de los primeros platos" << endl;
	for(int i = 0; i < numPrimersPlats; i++){
		int preuPlat = rand()%15;
		cout << "    (= (precio_plato primero" << i <<") " << preuPlat << ")" << endl;
	}
	cout <<
	"    ; Precios de los primeros platos" << endl;
	for(int i = 0; i < numSegonsPlats; i++){
		int preuPlat = rand()%15;
		cout << "    (= (precio_plato segundo" << i <<") " << preuPlat << ")" << endl;
	}


	cout << endl <<
	"    (= (precio_total) 0)" << endl <<
	"" << endl <<
	"" << endl <<
	"    )" << endl <<
	"" << endl <<
	"  (:metric " << endl <<
	"    minimize (precio_total)" << endl <<
	"    )" << endl <<
	"" << endl <<
	"  (:goal (forall (?d - dia)" << endl <<
	"                 (and (servido_primero ?d)" << endl <<
	"                      (servido_segundo ?d)" << endl <<
	"                      )" << endl <<
	"                 )" << endl <<
	"         )" << endl <<
	"  )" << endl;


	return 0;
}






