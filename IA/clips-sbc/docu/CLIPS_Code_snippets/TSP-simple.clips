;;; Travel Salesman Problem
;;; The dirty way

(deftemplate solucion
  (multislot camino)
  (slot coste (type INTEGER))
  (slot desc)
)


(deftemplate mat-dist
 (multislot dist)
)

(deffacts TSP
 (num-ciudades 10)
)


;;; Distancia entre dos ciudades
(deffunction calc-dist (?i ?j ?nciu ?m)
 (if (< ?i  ?j)
  then (bind ?pm ?i) (bind ?pn ?j)
  else (bind ?pm ?j) (bind ?pn ?i)
 )
 (bind ?off 0)
 (loop-for-count (?i 1 (- ?pm 1))
  do
  (bind ?off (+ ?off (- ?nciu ?i)))
 )
 (nth$ (+ ?off (- ?pn ?pm))  ?m)
)


;;; Coste de una solucion
(deffunction calc-coste (?s ?m)
 (bind ?c 0)
 (loop-for-count (?i 1 (- (length$ ?s) 1))
  do
  (bind ?c (+ ?c (calc-dist (nth$ ?i ?s) (nth$ (+ ?i 1) ?s) (length$ ?s) ?m )))
 )
 (bind ?c (+ ?c (calc-dist (nth$ 1 ?s) (nth$ (length$ ?s) ?s) (length$ ?s) ?m)))
)

;;; Inicializamos la matriz de distancias y los hechos para 
;;; hacer los intercambios
(defrule init
 (num-ciudades ?x)
=>
 (bind ?m (create$))
 (loop-for-count (?i 1 (/ (* ?x (- ?x 1)) 2))
  do
   (bind ?m (insert$ ?m ?i (+ (mod (random) 50) 1)))
 )
 (assert (mat-dist (dist ?m)))
 (loop-for-count (?i 1 ?x)
  do
  (assert (pos ?i))
 )
 (bind ?s (create$))
 (loop-for-count (?i 1 ?x)
  do
  (bind ?s (insert$ ?s ?i ?i)) 
 )
(assert (solucion (camino ?s) (coste (calc-coste ?s ?m)) (desc n)))
(printout t "Inicial ->"  ?s ": " (calc-coste ?s ?m) crlf)
)

;;; Comprueba todos los intercambios
(defrule HC-paso1
 (pos ?i)
 (pos ?j&:(> ?j ?i))
 (solucion (camino $?s) (coste ?c) (desc n))
 (mat-dist (dist $?m))
 =>
 (bind ?vi (nth$ ?i ?s))
 (bind ?vj (nth$ ?j ?s))
 (bind ?sm (delete$ ?s ?i ?i))
 (bind ?sm (insert$ ?sm ?i  ?vj))
 (bind ?sm (delete$ ?sm ?j ?j))
 (bind ?sm (insert$ ?sm ?j  ?vi))
 (bind ?nc (calc-coste ?sm ?m))
 ; (if (< ?nc ?c) 
 ;  then (assert (solucion (camino ?sm) (coste ?nc) (desc s))))
 (assert (solucion (camino ?sm) (coste ?nc) (desc s)))
)

;;; Actualiza la mejor solucion
(defrule HC-paso2
 (declare (salience -10))
 (solucion (camino $?s) (coste ?c) (desc s))
 (forall 
   (solucion  (coste ?oc) (desc s))
   (test (<= ?c ?oc)))
 ?solact <- (solucion (coste ?cs&:(< ?c ?cs)) (desc n)) 
 =>
  (modify ?solact (camino ?s) (coste ?c))
  (printout t "->"  ?s ": " ?c crlf)
)

;;; Imprime la solucion si no hay mejores valores
(defrule HC-es-fin
 (declare (salience -20))
 (solucion (camino $?s) (coste ?c) (desc n))
 =>
  (printout t "Final ->"  ?s ": " ?c crlf)
) 

