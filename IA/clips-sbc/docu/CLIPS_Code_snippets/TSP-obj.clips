;;; Travel Salesman Problem
;;; The OO way

(defclass mat-dist
  (is-a USER)
  (role concrete)
  (pattern-match reactive)
  (slot nciu)
  (multislot dist)
)

;;; Calcula la distancia entre dos ciudades
(defmessage-handler mat-dist calc-dist (?i ?j)
 (if (< ?i  ?j)
  then (bind ?pm ?i) (bind ?pn ?j)
  else (bind ?pm ?j) (bind ?pn ?j)
 )
 (bind ?off 0)
 (loop-for-count (?i 1 (- ?pm 1))
  do
  (bind ?off (+ ?off (- ?self:nciu ?i)))
 )
 (nth$ (+ ?off (- ?pn ?pm))  ?self:dist)
)


(defclass solucion
  (is-a USER)
  (role concrete)
  (pattern-match reactive)
  (multislot camino)
  (slot coste (type INTEGER))
  (slot desc)
)

;;; Coste de una solucion
(defmessage-handler solucion calc-coste (?m)
 (bind ?self:coste 0)
 (loop-for-count (?i 1 (- (length$ ?self:camino) 1))
  do
  (bind ?self:coste 
   (+ ?self:coste (send ?m calc-dist 
                       (nth$ ?i ?self:camino) 
                       (nth$ (+ ?i 1) ?self:camino))))
 )
 (bind ?self:coste 
    (+ ?self:coste (send ?m calc-dist 
                       (nth$ 1 ?self:camino) 
                       (nth$ (length$ ?self:camino) ?self:camino))))
)

;;; imprime una solucion
(defmessage-handler solucion imprime-sol ()
 (printout t ?self:camino ": " ?self:coste crlf )
)

;;; Intercambia dos ciudades
(defmessage-handler solucion intercambia (?i ?j ?m)
 (bind ?vi (nth$ ?i ?self:camino))
 (bind ?vj (nth$ ?j ?self:camino))
 (bind ?sm (delete$ ?self:camino ?i ?i))
 (bind ?sm (insert$ ?sm ?i  ?vj))
 (bind ?sm (delete$ ?sm ?j ?j))
 (bind ?sm (insert$ ?sm ?j  ?vi))
 (bind ?self:camino ?sm)
 (send ?self calc-coste ?m)
)



(deffacts TSP
 (num-ciudades 10)
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
 (bind ?md
   (make-instance (gensym) of mat-dist (dist ?m) (nciu ?x)))
 (loop-for-count (?i 1 ?x)
  do
  (assert (pos ?i))
 )
 (bind ?s (create$))
 (loop-for-count (?i 1 ?x)
  do
  (bind ?s (insert$ ?s ?i ?i)) 
 )
 (bind ?sol
  (make-instance (gensym) of solucion 
                         (camino ?s) (desc n)))
 (send ?sol calc-coste ?md)
(printout t "Inicial -> ")  
(send ?sol imprime-sol)
)

;;; Comprueba todas los intercambios
(defrule HC-paso1
 (pos ?i)
 (pos ?j&:(> ?j ?i))
 ?s <- (object (is-a solucion) (desc n))
 ?m <- (object (is-a mat-dist))
 =>
 (bind ?ns (duplicate-instance ?s to (gensym)))
 (send ?ns put-desc s)
 (send ?ns intercambia ?i ?j ?m)
 (if (< (send ?s get-coste) (send ?ns get-coste)) 
   then (send ?ns delete))
)

;;; Actualiza la mejor solucion
(defrule HC-paso2
 (declare (salience -10))
 ?solmejor <- (object (is-a solucion)  (coste ?c) (desc s))
 (forall 
   (object (is-a solucion) (coste ?oc) (desc s))
   (test (<= ?c ?oc)))
 ?solact <- (object (is-a solucion) (coste ?cs&:(< ?c ?cs)) (desc n)) 
 =>
  (send ?solmejor put-desc n)
  (send ?solact delete)
  (do-for-all-instances ((?sol solucion)) (eq ?sol:desc s)
   (send ?sol delete))
  (send ?solmejor imprime-sol)
)

;;; Imprime la solucion si no hay mejores valores
(defrule HC-es-fin
 (declare (salience -20))
 ?sol <- (object (is-a solucion) (desc n))
 =>
  (printout t "Final -> ")
  (send ?sol imprime-sol)
) 

